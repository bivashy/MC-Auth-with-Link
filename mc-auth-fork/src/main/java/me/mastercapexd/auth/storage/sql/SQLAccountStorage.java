package me.mastercapexd.auth.storage.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.common.collect.Sets;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.AccountFactory;
import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;

public abstract class SQLAccountStorage implements AccountStorage {

	private final PluginConfig config;
	private final AccountFactory accountFactory;

	private final String CREATE_TABLE, SELECT_BY_ID, SELECT_BY_VKID, SELECT_BY_LAST_QUIT_ORDERED, SELECT_VKIDs,
			SELECT_ALL,SELECT_ALL_LINKED, UPDATE_ID, DELETE;

	protected SQLAccountStorage(PluginConfig config, AccountFactory accountFactory, String cREATE_TABLE,
			String sELECT_BY_ID, String sELECT_BY_VKID, String sELECT_BY_LAST_QUIT_ORDERED, String sELECT_VKIDs,
			String sELECT_ALL,String sELECT_ALL_LINKED, String uPDATE_ID, String dELETE) {
		this.config = config;
		this.accountFactory = accountFactory;
		CREATE_TABLE = cREATE_TABLE;
		SELECT_BY_ID = sELECT_BY_ID;
		SELECT_BY_VKID = sELECT_BY_VKID;
		SELECT_BY_LAST_QUIT_ORDERED = sELECT_BY_LAST_QUIT_ORDERED;
		SELECT_VKIDs = sELECT_VKIDs;
		SELECT_ALL = sELECT_ALL;
		SELECT_ALL_LINKED = sELECT_ALL_LINKED;
		UPDATE_ID = uPDATE_ID;
		DELETE = dELETE;
	}

	protected abstract Connection getConnection() throws SQLException;

	protected void createTable() {
		try (Connection connection = this.getConnection()) {
			connection.createStatement().execute(CREATE_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void updateAccount(Account account) {
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(UPDATE_ID);
			statement.setString(1, account.getIdentifierType() == IdentifierType.NAME ? account.getId().toLowerCase()
					: account.getId());
			statement.setString(2, account.getUniqueId().toString());
			statement.setString(3, account.getName());
			statement.setString(4, account.getPasswordHash());
			statement.setInt(5, account.getVKId());
			statement.setLong(6, account.getLastQuitTime());
			statement.setString(7, account.getLastIpAddress());
			statement.setLong(8, account.getLastSessionStart());
			statement.setString(9, account.getIdentifierType().name());
			statement.setString(10, account.getHashType().name());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Account selectAccount(String id) {
		Account account = null;
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				account = accountFactory.createAccount(id, IdentifierType.valueOf(resultSet.getString("id_type")),
						UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"),
						HashType.valueOf(resultSet.getString("hash_type")), resultSet.getString("password"),
						resultSet.getInt("vkId"), resultSet.getLong("last_quit"), resultSet.getString("last_ip"),
						resultSet.getLong("last_session_start"), config.getSessionDurability());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	protected Collection<Account> selectAccountByVKID(Integer id) {
		Collection<Account> accounts = Sets.newHashSet();
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_VKID);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Account account = accountFactory.createAccount(resultSet.getString("id"),
						IdentifierType.valueOf(resultSet.getString("id_type")),
						UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"),
						HashType.valueOf(resultSet.getString("hash_type")), resultSet.getString("password"),
						resultSet.getInt("vkId"), resultSet.getLong("last_quit"), resultSet.getString("last_ip"),
						resultSet.getLong("last_session_start"), config.getSessionDurability());
				accounts.add(account);
			}
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
	public CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id) {
		return CompletableFuture.supplyAsync(() -> selectAccountByVKID(id));
	}

	@Override
	public CompletableFuture<Collection<Account>> getAccounts(int limit) {
		return CompletableFuture.supplyAsync(() -> {
			Collection<Account> accounts = Sets.newHashSet();
			try (Connection connection = this.getConnection()) {
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_LAST_QUIT_ORDERED);
				statement.setInt(1, limit);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Account account = accountFactory.createAccount(resultSet.getString("id"),
							IdentifierType.valueOf(resultSet.getString("id_type")),
							UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"),
							HashType.valueOf(resultSet.getString("hash_type")), resultSet.getString("password"),
							resultSet.getInt("vkId"), resultSet.getLong("last_quit"), resultSet.getString("last_ip"),
							resultSet.getLong("last_session_start"), config.getSessionDurability());
					accounts.add(account);
				}
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
				while (resultSet.next()) {
					Account account = accountFactory.createAccount(resultSet.getString("id"),
							IdentifierType.valueOf(resultSet.getString("id_type")),
							UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"),
							HashType.valueOf(resultSet.getString("hash_type")), resultSet.getString("password"),
							resultSet.getInt("vkId"), resultSet.getLong("last_quit"), resultSet.getString("last_ip"),
							resultSet.getLong("last_session_start"), config.getSessionDurability());
					accounts.add(account);
				}
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
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_LINKED);
				while (resultSet.next()) {
					Account account = accountFactory.createAccount(resultSet.getString("id"),
							IdentifierType.valueOf(resultSet.getString("id_type")),
							UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"),
							HashType.valueOf(resultSet.getString("hash_type")), resultSet.getString("password"),
							resultSet.getInt("vkId"), resultSet.getLong("last_quit"), resultSet.getString("last_ip"),
							resultSet.getLong("last_session_start"), config.getSessionDurability());
					accounts.add(account);
				}
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
					Integer vkId = resultSet.getInt("vkId");
					if (vkId == null || vkId == -1)
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
}