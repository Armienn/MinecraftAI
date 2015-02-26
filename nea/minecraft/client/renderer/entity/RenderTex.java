package nea.minecraft.client.renderer.entity;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.client.model.ModelTestMob;
import nea.minecraft.client.model.baby_Creature;
import nea.minecraft.client.model.teen_Creature;
import nea.minecraft.client.model.adult_Creature;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderTex extends RenderLiving
{
    private static final ResourceLocation field_177120_j = new ResourceLocation("textures/entity/baby_tex/baby_tex.png"); //baby tex
    //private static final ResourceLocation field_177120_j = new ResourceLocation("textures/entity/teen_tex/teen_tex.png"); //teen tex
    //private static final ResourceLocation field_177120_j = new ResourceLocation("textures/entity/adult_tex/adult_tex.png"); //adult tex
    
    public RenderTex(RenderManager p_i46148_1_)
    {
        super(p_i46148_1_, new baby_Creature(), 0.5F);
        this.addLayer(new LayerHeldItem(this));
        
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void func_177177_a()
            {
                this.field_177189_c = new baby_Creature();
                this.field_177186_d = new baby_Creature();
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
