package nea.minecraft.client.renderer.entity;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.client.model.ModelTestMob;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderTex extends RenderBiped
{
    private static final ResourceLocation field_177120_j = new ResourceLocation("textures/entity/zombie_pigman.png");

    public RenderTex(RenderManager p_i46148_1_)
    {
        super(p_i46148_1_, new ModelTestMob(), 0.5F, 1.0F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void func_177177_a()
            {
                this.field_177189_c = new ModelTestMob(0.5F, true);
                this.field_177186_d = new ModelTestMob(1.0F, true);
            }
        });
    }

    protected ResourceLocation func_177119_a(EntityTex p_177119_1_)
    {
        return field_177120_j;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_)
    {
        return field_177120_j;//this.func_177119_a((TestMob)p_110775_1_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.func_177119_a((EntityTex)p_110775_1_);
    }
}
