package dev.quarris.projecta.content.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import dev.quarris.projecta.registry.Content;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import quarris.qlib.api.client.helper.ColorHelper;

public class BubblingParticleOptions implements ParticleOptions {

    public static final ParticleOptions.Deserializer<BubblingParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public BubblingParticleOptions fromCommand(ParticleType<BubblingParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int color = ColorHelper.toColor(reader.readInt(), reader.readInt(), reader.readInt(), reader.readInt());
            return new BubblingParticleOptions(color);
        }

        public BubblingParticleOptions fromNetwork(ParticleType<BubblingParticleOptions> type, FriendlyByteBuf buffer) {
            return new BubblingParticleOptions(buffer.readVarInt());
        }
    };

    public static Codec<BubblingParticleOptions> codec(ParticleType<BubblingParticleOptions> type) {
        return Codec.INT.xmap(BubblingParticleOptions::new, (x) -> x.color);
    }

    public final int color;

    public BubblingParticleOptions(int color) {
        this.color = color;
    }

    @Override
    public ParticleType<?> getType() {
        return Content.PARTICLE_TYPES.bubbling.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.color);
    }

    @Override
    public String writeToString() {
        int[] rgba = ColorHelper.fromColori(this.color);
        return this.getType().getRegistryName() + " " + rgba[0] + " " + rgba[1] + " " + rgba[2] + " " + rgba[3];
    }
}
