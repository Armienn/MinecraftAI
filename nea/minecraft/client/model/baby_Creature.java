// Date: 26-02-2015 10:10:12
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package nea.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;

public class baby_Creature extends ModelBase
{
  //fields
    ModelRenderer Leg1;
    ModelRenderer Body;
    ModelRenderer Leg2;
    ModelRenderer Leg3;
    ModelRenderer Leg4;
    ModelRenderer Spike1;
    ModelRenderer Spike2;
    ModelRenderer LeftarmTL;
    ModelRenderer LeftarmTR;
    ModelRenderer LeftarmBL;
    ModelRenderer LeftarmBR;
    ModelRenderer RightarmTL;
    ModelRenderer RightarmTR;
    ModelRenderer RightarmBL;
    ModelRenderer RightarmBR;
  
  public baby_Creature()
  {
    textureWidth = 32;
    textureHeight = 36;
    
      Leg1 = new ModelRenderer(this, 0, 16);
      Leg1.addBox(-1.5F, 0F, -1.5F, 3, 3, 3);
      Leg1.setRotationPoint(2.5F, 21F, 2.5F);
      Leg1.setTextureSize(32, 36);
      Leg1.mirror = true;
      setRotation(Leg1, 0F, 0F, 0F);
      Body = new ModelRenderer(this, 0, 0);
      Body.addBox(-4F, -4F, -4F, 8, 8, 8);
      Body.setRotationPoint(0F, 17F, 0F);
      Body.setTextureSize(32, 36);
      Body.mirror = true;
      setRotation(Body, 0F, 0F, 0F);
      Leg2 = new ModelRenderer(this, 12, 16);
      Leg2.addBox(-1.5F, 0F, -1.5F, 3, 3, 3);
      Leg2.setRotationPoint(-2.5F, 21F, 2.5F);
      Leg2.setTextureSize(32, 36);
      Leg2.mirror = true;
      setRotation(Leg2, 0.0174533F, 0F, 0F);
      Leg3 = new ModelRenderer(this, 0, 22);
      Leg3.addBox(-1.5F, 0F, -1.5F, 3, 3, 3);
      Leg3.setRotationPoint(2.5F, 21F, -2.5F);
      Leg3.setTextureSize(32, 36);
      Leg3.mirror = true;
      setRotation(Leg3, 0F, 0F, 0F);
      Leg4 = new ModelRenderer(this, 12, 22);
      Leg4.addBox(-1.5F, 0F, -1.5F, 3, 3, 3);
      Leg4.setRotationPoint(-2.5F, 21F, -2.5F);
      Leg4.setTextureSize(32, 36);
      Leg4.mirror = true;
      setRotation(Leg4, 0F, 0F, 0F);
      Spike1 = new ModelRenderer(this, 24, 19);
      Spike1.addBox(-0.5F, 0F, -1.5F, 1, 1, 1);
      Spike1.setRotationPoint(0F, 12F, -1F);
      Spike1.setTextureSize(32, 36);
      Spike1.mirror = true;
      setRotation(Spike1, 0F, 0F, 0F);
      Spike2 = new ModelRenderer(this, 24, 16);
      Spike2.addBox(0F, 0F, 0F, 1, 2, 1);
      Spike2.setRotationPoint(-0.5F, 11F, -1F);
      Spike2.setTextureSize(32, 36);
      Spike2.mirror = true;
      setRotation(Spike2, 0F, 0F, 0F);
      LeftarmTL = new ModelRenderer(this, 8, 28);
      LeftarmTL.addBox(-1F, -1F, -1F, 2, 2, 2);
      LeftarmTL.setRotationPoint(5F, 15F, -2F);
      LeftarmTL.setTextureSize(32, 36);
      LeftarmTL.mirror = true;
      setRotation(LeftarmTL, 0F, 0F, 0F);
      LeftarmTR = new ModelRenderer(this, 0, 28);
      LeftarmTR.addBox(-1F, -1F, -1F, 2, 2, 2);
      LeftarmTR.setRotationPoint(5F, 15F, 2F);
      LeftarmTR.setTextureSize(32, 36);
      LeftarmTR.mirror = true;
      setRotation(LeftarmTR, 0F, 0F, 0F);
      LeftarmBL = new ModelRenderer(this, 0, 32);
      LeftarmBL.addBox(-1F, -1F, -1F, 2, 2, 2);
      LeftarmBL.setRotationPoint(5F, 19F, -2F);
      LeftarmBL.setTextureSize(32, 36);
      LeftarmBL.mirror = true;
      setRotation(LeftarmBL, 0F, 0F, 0F);
      LeftarmBR = new ModelRenderer(this, 8, 32);
      LeftarmBR.addBox(-1F, -1F, -1F, 2, 2, 2);
      LeftarmBR.setRotationPoint(5F, 19F, 2F);
      LeftarmBR.setTextureSize(32, 36);
      LeftarmBR.mirror = true;
      setRotation(LeftarmBR, 0F, 0F, 0F);
      RightarmTL = new ModelRenderer(this, 16, 28);
      RightarmTL.addBox(-1F, -1F, -1F, 2, 2, 2);
      RightarmTL.setRotationPoint(-5F, 15F, 2F);
      RightarmTL.setTextureSize(32, 36);
      RightarmTL.mirror = true;
      setRotation(RightarmTL, 0F, 0F, 0F);
      RightarmTR = new ModelRenderer(this, 16, 32);
      RightarmTR.addBox(-1F, -1F, -1F, 2, 2, 2);
      RightarmTR.setRotationPoint(-5F, 15F, -2F);
      RightarmTR.setTextureSize(32, 36);
      RightarmTR.mirror = true;
      setRotation(RightarmTR, 0F, 0F, 0F);
      RightarmBL = new ModelRenderer(this, 24, 28);
      RightarmBL.addBox(-1F, -1F, -1F, 2, 2, 2);
      RightarmBL.setRotationPoint(-5F, 19F, 2F);
      RightarmBL.setTextureSize(32, 36);
      RightarmBL.mirror = true;
      setRotation(RightarmBL, 0F, 0F, 0F);
      RightarmBR = new ModelRenderer(this, 24, 32);
      RightarmBR.addBox(-1F, -1F, -1F, 2, 2, 2);
      RightarmBR.setRotationPoint(-5F, 19F, -2F);
      RightarmBR.setTextureSize(32, 36);
      RightarmBR.mirror = true;
      setRotation(RightarmBR, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Leg1.render(f5);
    Body.render(f5);
    Leg2.render(f5);
    Leg3.render(f5);
    Leg4.render(f5);
    Spike1.render(f5);
    Spike2.render(f5);
    LeftarmTL.render(f5);
    LeftarmTR.render(f5);
    LeftarmBL.render(f5);
    LeftarmBR.render(f5);
    RightarmTL.render(f5);
    RightarmTR.render(f5);
    RightarmBL.render(f5);
    RightarmBR.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
