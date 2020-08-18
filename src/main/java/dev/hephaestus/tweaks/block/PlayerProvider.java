package dev.hephaestus.tweaks.block;

import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerProvider {
	ThreadLocal<PlayerProvider> STATIC = new ThreadLocal<PlayerProvider>() {{
		this.set(new PlayerProvider() {
			private ServerPlayerEntity playerEntity = null;

			@Override
			public ServerPlayerEntity getPlayer() {
				return this.playerEntity;
			}

			@Override
			public void setPlayer(ServerPlayerEntity player) {
				this.playerEntity = player;
			}
		});
	}};

	ServerPlayerEntity getPlayer();
	void setPlayer(ServerPlayerEntity player);
}
