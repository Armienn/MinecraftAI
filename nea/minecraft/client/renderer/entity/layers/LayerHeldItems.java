package nea.minecraft.client.renderer.entity.layers;

import nea.minecraft.tex.EntityTex;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItems implements LayerRenderer
{
    private final RendererLivingEntity field_177206_a;
    private static final String __OBFID = "CL_00002416";
    EntityTex tex;
    
    public LayerHeldItems(RendererLivingEntity p_i46115_1_)
    {
        this.field_177206_a = p_i46115_1_;
    }

    public void doRenderLayer(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
    	tex = (EntityTex)entity;
    	
    	for(int i=0;i<8;i++){
    		ItemStack stack = tex.inventory[i];
	    	
    		if (stack != null)
	        {
	            GlStateManager.pushMatrix();
	
	            /*if (this.field_177206_a.getMainModel().isChild)
	            {
	                float var10 = 0.5F;
	                GlStateManager.translate(0.0F, 0.625F, 0.0F);
	                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
	                GlStateManager.scale(var10, var10, var10);
	            }*/
	
	            //((ModelBiped)this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0625F);
	            float scale = 0.5F;
	            GlStateManager.scale(scale, scale, scale);
	            //GlStateManager.translate(0F, -0.125F, 0F);
	            if(i == 0){
	            	GlStateManager.translate(-0.525F, 0.1375F, 0.425F);
	            }
	            else if(i == 1){
	            	GlStateManager.translate(-0.525F, 0.1375F, -0.425F);
	            	}
	            else if(i == 2){
	            	GlStateManager.translate(0.525F, 0.1375F, 0.425F);
	            }
	            else if(i == 3){
	            	GlStateManager.translate(0.525F, 0.1375F, -0.425F);
	            }
	            else if(i == 4){
	            	GlStateManager.translate(-0.525F, 0.3375F, 0.425F);
	            }
	            else if(i == 5){
	            	GlStateManager.translate(-0.525F, 0.3375F, -0.425F);
	            }
	            else if(i == 6){
	            	GlStateManager.translate(0.525F, 0.3375F, 0.425F);
	            }
	            else if(i == 7){
	            	GlStateManager.translate(0.525F, 0.3375F, -0.425F);
	            }
	
	            Item item = stack.getItem();
	            Minecraft var11 = Minecraft.getMinecraft();
	
	            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2)
	            {
	                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
	                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
	                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
	                float var12 = 0.375F;
	                GlStateManager.scale(-var12, -var12, var12);
	            }
	
	            var11.getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.THIRD_PERSON);
	            GlStateManager.popMatrix();
	        }
    	}

        
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
