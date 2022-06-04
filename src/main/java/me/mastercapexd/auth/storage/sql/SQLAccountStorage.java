package me.mastercapexd.auth.storage.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Sets;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.telegram.TelegramLinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.link.vk.VKLinkUser;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.storage.column.AlterColumnUpdate;
import me.mastercapexd.auth.storage.column.DropColumnUpdate;
import me.mastercapexd.auth.storage.column.ExecuteColumnStatementUpdate;
import me.mastercapexd.auth.storage.column.StorageUpdate;

public abstract class SQLAccountStorage implements AccountStorage {
	private static final String ACCOUNT_ID_COLUMN_KEY = "id";
	private static final String ID_TYPE_COLUMN_KEY = "id_type";
	private static final String UNIQUE_ID_COLUMN_KEY = "uuid";
	private static final String NICKNAME_COLUMN_KEY = "name";
	private static final String HASH_TYPE_COLUMN_KEY = "hash_type";
	private static final String PASSWORD_COLUMN_KEY = "password";
	private static final String GOOGLE_KEY_COLUMN_KEY = "google_key";
	private static final String VK_ID_COLUMN_KEY = "vkId";
	private static final String VK_CONFIRMATION_ENABLED_COLUMN_KEY = "vk_confirmation_enabled";
	private static final String TELEGRAM_ID_COLUMN_KEY = "telegram_id";
	private static final String TELEGRAM_CONFIRMATION_ENABLED_COLUMN_KEY = "telegram_confirmation_enabled";
	private static final String LAST_QUIT_COLUMN_KEY = "last_quit";
	private static final String LAST_IP_COLUMN_KEY = "last_ip";
	private static final String LAST_SESSION_START_COLUMN_KEY = "last_session_start";

	private final PluginConfig config;
	private final AccountFactory accountFactory;

	private final String CREATE_TABLE, SELECT_BY_ID, SELECT_BY_NAME, SELECT_BY_VKID, SELECT_BY_LINK_ID,
			SELECT_BY_LAST_QUIT_ORDERED, SELECT_VKIDs, SELECT_ALL, SELECT_ALL_LINKED, UPDATE_ID, DELETE;

	private final List<StorageUpdate> columnUpdates = Arrays.asList(
			new AlterColumnUpdate(GOOGLE_KEY_COLUMN_KEY, "VARCHAR(64)"),
			new AlterColumnUpdate(VK_CONFIRMATION_ENABLED_COLUMN_KEY, "BIT"),
			new AlterColumnUpdate(TELEGRAM_ID_COLUMN_KEY, "BIGINT", String.valueOf(AccountFactory.DEFAULT_TELEGRAM_ID)),
			new AlterColumnUpdate(TELEGRAM_CONFIRMATION_ENABLED_COLUMN_KEY, "BIT"),
			new ExecuteColumnStatementUpdate("vk_confirm_enabled",
					"UPDATE `auth` SET `vk_confirmation_enabled` = CASE WHEN `vk_confirm_enabled` = 'true' THEN '1' WHEN `vk_confirm_enabled` = 'false' THEN '0' ELSE 0 END;"),
			new DropColumnUpdate("vk_confirm_enabled"));

	protected SQLAccountStorage(PluginConfig config, AccountFactory accountFactory, String CREATE_TABLE,
			String SELECT_BY_ID, String SELECT_BY_NAME, String SELECT_BY_VKID, String SELECT_BY_LINK_ID,
			String SELECT_BY_LAST_QUIT_ORDERED, String SELECT_VKIDs, String SELECT_ALL, String SELECT_ALL_LINKED,
			String UPDATE_ID, String DELETE) {
		this.config = config;
		this.accountFactory = accountFactory;
		this.CREATE_TABLE = CREATE_TABLE;
		this.SELECT_BY_ID = SELECT_BY_ID;
		this.SELECT_BY_NAME = SELECT_BY_NAME;
		this.SELECT_BY_VKID = SELECT_BY_VKID;
		this.SELECT_BY_LINK_ID = SELECT_BY_LINK_ID;
		this.SELECT_BY_LAST_QUIT_ORDERED = SELECT_BY_LAST_QUIT_ORDERED;
		this.SELECT_VKIDs = SELECT_VKIDs;
		this.SELECT_ALL = SELECT_ALL;
		this.SELECT_ALL_LINKED = SELECT_ALL_LINKED;
		this.UPDATE_ID = UPDATE_ID;
		this.DELETE = DELETE;
	}

	protected abstract Connection getConnection() throws SQLException;

	protected void createTable() {
		try (Connection connection = this.getConnection()) {
			connection.createStatement().execute(CREATE_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void createColumns() {
		columnUpdates.forEach(column -> {
			try (Connection connection = this.getConnection()) {
				column.apply(connection);
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	protected void updateAccount(Account account) {
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(UPDATE_ID);

			LinkUserInfo telegramLinkInfo = account.findFirstLinkUser(TelegramLinkType.LINK_USER_FILTER)
					.orElse(new TelegramLinkUser(account, AccountFactory.DEFAULT_TELEGRAM_ID)).getLinkUserInfo();
			LinkUserInfo vkLinkInfo = account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER)
					.orElse(new VKLinkUser(account, AccountFactory.DEFAULT_VK_ID)).getLinkUserInfo();
			LinkUserInfo googleLinkInfo = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER)
					.orElse(new GoogleLinkUser(account, AccountFactory.DEFAULT_GOOGLE_KEY)).getLinkUserInfo();

			statement.setString(1, account.getIdentifierType().fromRawString(account.getId()));
			statement.setString(2, account.getUniqueId().toString());
			statement.setString(3, account.getName());
			statement.setString(4, account.getPasswordHash());
			statement.setString(5, googleLinkInfo.getIdentificator().asString());
			statement.setInt(6, (int) vkLinkInfo.getIdentificator().asNumber());
			statement.setBoolean(7, vkLinkInfo.getConfirmationState().shouldSendConfirmation());
			statement.setLong(8, telegramLinkInfo.getIdentificator().asNumber());
			statement.setBoolean(9, telegramLinkInfo.getConfirmationState().shouldSendConfirmation());
			statement.setLong(10, account.getLastQuitTime());
			statement.setString(11, account.getLastIpAddress());
			statement.setLong(12, account.getLastSessionStart());
			statement.setString(13, account.getIdentifierType().name());
			statement.setString(14, account.getHashType().name());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Account selectAccount(String id) {
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return createFromResult(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Account selectAccountFromName(String id) {
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return createFromResult(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Collection<Account> selectAccountByVKID(Integer id) {
		Collection<Account> accounts = Sets.newHashSet();
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_VKID);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
				accounts.add(createFromResult(resultSet));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	protected Collection<Account> selectAccountFromLinkIdentificator(LinkUserIdentificator identificator) {
		Collection<Account> accounts = Sets.newHashSet();
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_LINK_ID);
			if (identificator.isNumber()) {
				UserNumberIdentificator numberIdentificator = identificator.safeAs(UserNumberIdentificator.class).get();
				if (numberIdentificator.isLong()) {
					statement.setLong(1, numberIdentificator.asNumber());
				} else {
					statement.setInt(1, (int) numberIdentificator.asNumber());
				}
			} else {
				statement.setString(1, identificator.asString());
			}
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
				accounts.add(createFromResult(resultSet));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public void saveOrUpdateAccount(Account account) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> updateAccount(account));
		executorService.shutdown();
	}

	@Override
	public CompletableFuture<Account> getAccount(String id) {
		return CompletableFuture.supplyAsync(() -> selectAccount(id));
	}

	@Override
	public CompletableFuture<Account> getAccountFromName(String playerName) {
		return CompletableFuture.supplyAsync(() -> selectAccountFromName(playerName));
	}

	@Override
	public CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id) {
		return CompletableFuture.supplyAsync(() -> selectAccountByVKID(id));
	}

	@Override
	public CompletableFuture<Collection<Account>> getAccountsFromLinkIdentificator(
			LinkUserIdentificator identificator) {
		return CompletableFuture.supplyAsync(() -> selectAccountFromLinkIdentificator(identificator));
	}

	@Override
	public CompletableFuture<Collection<Account>> getAccounts(int limit) {
		return CompletableFuture.supplyAsync(() -> {
			Collection<Account> accounts = Sets.newHashSet();
			try (Connection connection = this.getConnection()) {
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_LAST_QUIT_ORDERED);
				statement.setInt(1, limit);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
					accounts.add(createFromResult(resultSet));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return accounts;
		});
	}

	@Override
	public CompletableFuture<Collection<Account>> getAllAccounts() {
		return CompletableFuture.supplyAsync(() -> {
			Collection<Account> accounts = Sets.newHashSet();
			try (Connection connection = this.getConnection()) {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL);
				while (resultSet.next())
					accounts.add(createFromResult(resultSet));

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return accounts;
		});
	}

	@Override
	public CompletableFuture<Collection<Account>> getAllLinkedAccounts() {
		return CompletableFuture.supplyAsync(() -> {
			Collection<Account> accounts = Sets.newHashSet();
			try (Connection connection = this.getConnection()) {
				PreparedStatement statement = connection.prepareStatement(SELECT_ALL_LINKED);
				statement.setInt(1, AccountFactory.DEFAULT_VK_ID);
				statement.setLong(2, AccountFactory.DEFAULT_TELEGRAM_ID);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
					accounts.add(createFromResult(resultSet));

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return accounts;
		});
	}

	@Override
	public CompletableFuture<Collection<Integer>> getVKIDs() {
		return CompletableFuture.supplyAsync(() -> {
			Collection<Integer> vkIDs = Sets.newHashSet();
			try (Connection connection = this.getConnection()) {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_VKIDs);
				while (resultSet.next()) {
					Integer vkId = resultSet.getInt(VK_ID_COLUMN_KEY);
					if (vkId == null || vkId == AccountFactory.DEFAULT_VK_ID)
						continue;
					vkIDs.add(vkId);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return vkIDs;
		});
	}

	@Override
	public void deleteAccount(String id) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> {
			try (Connection connection = this.getConnection()) {
				connection.createStatement().execute(DELETE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		executorService.shutdown();
	}

	private Account createFromResult(ResultSet resultSet) throws SQLException {
		return accountFactory.createAccount(resultSet.getString(ACCOUNT_ID_COLUMN_KEY),
				IdentifierType.valueOf(resultSet.getString(ID_TYPE_COLUMN_KEY)),
				UUID.fromString(resultSet.getString(UNIQUE_ID_COLUMN_KEY)), resultSet.getString(NICKNAME_COLUMN_KEY),
				HashType.valueOf(resultSet.getString(HASH_TYPE_COLUMN_KEY)), resultSet.getString(PASSWORD_COLUMN_KEY),
				resultSet.getString(GOOGLE_KEY_COLUMN_KEY), resultSet.getInt(VK_ID_COLUMN_KEY),
				resultSet.getBoolean(VK_CONFIRMATION_ENABLED_COLUMN_KEY), resultSet.getLong(TELEGRAM_ID_COLUMN_KEY),
				resultSet.getBoolean(TELEGRAM_CONFIRMATION_ENABLED_COLUMN_KEY), resultSet.getLong(LAST_QUIT_COLUMN_KEY),
				resultSet.getString(LAST_IP_COLUMN_KEY), resultSet.getLong(LAST_SESSION_START_COLUMN_KEY),
				config.getSessionDurability());
	}
}