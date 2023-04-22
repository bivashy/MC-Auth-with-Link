package me.mastercapexd.auth.link.vk;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.lamp.commands.vk.message.DispatchSource;
import com.bivashy.messenger.common.identificator.Identificator;
import com.bivashy.messenger.common.message.Message;
import com.bivashy.lamp.commands.vk.VkActor;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeerType;
import com.vk.api.sdk.objects.users.UserFull;

import me.mastercapexd.auth.link.LinkCommandActorWrapperTemplate;

public class VKCommandActorWrapper extends LinkCommandActorWrapperTemplate<VkActor> implements VkActor {
    public VKCommandActorWrapper(VkActor actor) {
        super(actor);
    }

    @Override
    public void send(Message message) {
        message.send(Identificator.of(actor.getPeerId()));
    }

    @Override
    public LinkUserIdentificator userId() {
        return new UserNumberIdentificator(actor.getAuthorId());
    }

    @Override
    public DispatchSource getDispatchSource() {
        return actor.getDispatchSource();
    }

    @Override
    public UserFull getUser() {
        return actor.getUser();
    }

    @Override
    public Conversation getConversation() {
        return actor.getConversation();
    }

    @Override
    public ConversationPeerType getConversationType() {
        return actor.getConversationType();
    }

    @Override
    public String getText() {
        return actor.getText();
    }

    @Override
    public String getMessagePayload() {
        return actor.getMessagePayload();
    }

    @Override
    public Integer getConversationId() {
        return actor.getConversationId();
    }

    @Override
    public Integer getAuthorId() {
        return actor.getAuthorId();
    }

    @Override
    public Integer getPeerId() {
        return actor.getPeerId();
    }
}
