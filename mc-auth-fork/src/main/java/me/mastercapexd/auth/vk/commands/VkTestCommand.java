package me.mastercapexd.auth.vk.commands;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;

@Command("test")
public class VkTestCommand {
	@Default
	public void testCommand(LinkCommandActorWrapper actorWrapper, LinkType linkType) {
		actorWrapper.reply("Test reply");
		actorWrapper.send(linkType.newMessageBuilder().rawContent("test custom message system").build());
		actorWrapper
				.send(linkType.newMessageBuilder().rawContent("test keyboard")
						.keyboard(linkType.newKeyboardBuilder()
								.button(0, linkType.newButtonBuilder().label("test").build()).inline(true).build())
						.build());

		actorWrapper
				.send(linkType.newMessageBuilder().rawContent("123")
						.keyboard(linkType.newKeyboardBuilder().button(0,
								linkType.newButtonBuilder().action(linkType.newButtonActionBuilder().callback("test"))
										.color(linkType.newButtonColorBuilder().blue()).label("Тест").build())
								.build())
						.build());
	}
}
