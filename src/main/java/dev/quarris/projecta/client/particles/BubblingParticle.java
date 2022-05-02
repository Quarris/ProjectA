package dev.quarris.projecta.client.particles;

import dev.quarris.projecta.content.particles.BubblingParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.Nullable;
import quarris.qlib.api.client.helper.ColorHelper;

public class BubblingParticle extends TextureSheetParticle {

    private SpriteSet sprites;
    private double endX, endY, endZ;

    protected BubblingParticle(SpriteSet sprites, ClientLevel level, double x, double y, double z, float r, float g, float b, float a) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.endX = x;
        this.endY = y;
        this.endZ = z;

        this.setSpriteFromAge(this.sprites);
        this.setPos(x, y - 0.1, z);

        this.setLifetime(7 + (int) (Math.random() * 5));
        this.yo = y - 0.1;
        this.yd = 0.01 + Math.random() / 100;

        this.scale(0.45f);
        this.setColor(r, g, b);
        this.setAlpha(a);
        this.hasPhysics = false;
        this.roll = (float)Math.random() * ((float)Math.PI * 2F);
        this.oRoll = this.roll;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.sprites);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;

            if (this.y >= this.endY) {
                this.remove();
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<BubblingParticleOptions> {

        private SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(BubblingParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            float[] rgba = ColorHelper.fromColorf(options.color);
            BubblingParticle particle = new BubblingParticle(this.sprites, pLevel, pX, pY, pZ, rgba[0], rgba[1], rgba[2], rgba[3]);
            return particle;
        }
    }
}
