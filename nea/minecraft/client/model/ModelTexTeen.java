// Date: 26-02-2015 11:23:21
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package nea.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;

public class ModelTexTeen extends ModelBase
{
  //fields
    ModelRenderer Body;
    ModelRenderer Backbody;
    ModelRenderer tail;
    ModelRenderer tail2;
    ModelRenderer RightlegB;
    ModelRenderer LeftlegB;
    ModelRenderer RightlegF;
    ModelRenderer LeftlegF;
    ModelRenderer Head;
    ModelRenderer Nose;
    ModelRenderer RightarmLB;
    ModelRenderer RightarmUB;
    ModelRenderer RightarmLF;
    ModelRenderer RightarmUF;
    ModelRenderer LeftarmLB;
    ModelRenderer LeftarmUB;
    ModelRenderer LeftarmLF;
    ModelRenderer LeftarmUF;
    ModelRenderer Neck;
    ModelRenderer Taillast;
    ModelRenderer Spikebody;
    ModelRenderer Spikebodyback;
    ModelRenderer SpikeTail;
    ModelRenderer SpikeTail1;
    ModelRenderer Spiketail2;
    ModelRenderer Spikehead;
  
  public ModelTexTeen()
  {
    textureWidth = 61;
    textureHeight = 53;
    
      Body = new ModelRenderer(this, 31, 0);
      Body.addBox(-3F, -3F, -3F, 7, 6, 8);
      Body.setRotationPoint(0F, 15F, -1F);
      Body.setTextureSize(61, 53);
      Body.mirror = true;
      setRotation(Body, 0F, 0F, 0F);
      Backbody = new ModelRenderer(this, 38, 24);
      Backbody.addBox(-2F, -2F, -2F, 5, 5, 4);
      Backbody.setRotationPoint(0F, 15F, 6F);
      Backbody.setTextureSize(61, 53);
      Backbody.mirror = true;
      setRotation(Backbody, 0F, 0F, 0F);
      tail = new ModelRenderer(this, 22, 25);
      tail.addBox(-1.5F, -1.5F, -2F, 4, 4, 4);
      tail.setRotationPoint(0F, 15.5F, 10F);
      tail.setTextureSize(61, 53);
      tail.mirror = true;
      setRotation(tail, 0F, 0F, 0F);
      tail2 = new ModelRenderer(this, 10, 27);
      tail2.addBox(-1F, -1F, -1F, 3, 3, 3);
      tail2.setRotationPoint(0F, 16F, 13F);
      tail2.setTextureSize(61, 53);
      tail2.mirror = true;
      setRotation(tail2, 0F, 0F, 0F);
      RightlegB = new ModelRenderer(this, 29, 45);
      RightlegB.addBox(-1F, -1.5F, -1F, 2, 6, 2);
      RightlegB.setRotationPoint(-2F, 19.5F, 2F);
      RightlegB.setTextureSize(61, 53);
      RightlegB.mirror = true;
      setRotation(RightlegB, 0F, 0F, 0F);
      LeftlegB = new ModelRenderer(this, 44, 45);
      LeftlegB.addBox(-1F, -1.5F, -1F, 2, 6, 2);
      LeftlegB.setRotationPoint(3F, 19.5F, 2F);
      LeftlegB.setTextureSize(61, 53);
      LeftlegB.mirror = true;
      setRotation(LeftlegB, 0F, 0F, 0F);
      RightlegF = new ModelRenderer(this, 18, 37);
      RightlegF.addBox(-1F, -1.5F, -1F, 2, 6, 2);
      RightlegF.setRotationPoint(-2F, 19.5F, -2F);
      RightlegF.setTextureSize(61, 53);
      RightlegF.mirror = true;
      setRotation(RightlegF, 0F, 0F, 0F);
      LeftlegF = new ModelRenderer(this, 36, 45);
      LeftlegF.addBox(-1F, -1.5F, -1F, 2, 6, 2);
      LeftlegF.setRotationPoint(3F, 19.5F, -2F);
      LeftlegF.setTextureSize(61, 53);
      LeftlegF.mirror = true;
      setRotation(LeftlegF, 0F, 0F, 0F);
      Head = new ModelRenderer(this, 0, 0);
      Head.addBox(-1F, -2F, -1F, 5, 5, 5);
      Head.setRotationPoint(-1F, 14F, -10F);
      Head.setTextureSize(61, 53);
      Head.mirror = true;
      setRotation(Head, 0F, 0F, 0F);
      Nose = new ModelRenderer(this, 0, 10);
      Nose.addBox(-1.5F, -0.5F, -1.5F, 3, 3, 3);
      Nose.setRotationPoint(0.5F, 14.5F, -12F);
      Nose.setTextureSize(61, 53);
      Nose.mirror = true;
      setRotation(Nose, 0F, 0F, 0F);
      RightarmLB = new ModelRenderer(this, 17, 45);
      RightarmLB.addBox(-1F, -1F, -1F, 4, 2, 2);
      RightarmLB.setRotationPoint(-6F, 17F, 2F);
      RightarmLB.setTextureSize(61, 53);
      RightarmLB.mirror = true;
      setRotation(RightarmLB, 0F, 0F, 0F);
      RightarmUB = new ModelRenderer(this, 6, 46);
      RightarmUB.addBox(-1F, -1F, -1F, 4, 2, 2);
      RightarmUB.setRotationPoint(-6F, 13F, 2F);
      RightarmUB.setTextureSize(61, 53);
      RightarmUB.mirror = true;
      setRotation(RightarmUB, 0F, 0F, 0F);
      RightarmLF = new ModelRenderer(this, 17, 49);
      RightarmLF.addBox(-1F, -1F, -1F, 4, 2, 2);
      RightarmLF.setRotationPoint(-6F, 17F, -2F);
      RightarmLF.setTextureSize(61, 53);
      RightarmLF.mirror = true;
      setRotation(RightarmLF, 0F, 0F, 0F);
      RightarmUF = new ModelRenderer(this, 6, 49);
      RightarmUF.addBox(-1F, -1F, -1F, 4, 2, 2);
      RightarmUF.setRotationPoint(-6F, 13F, -2F);
      RightarmUF.setTextureSize(61, 53);
      RightarmUF.mirror = true;
      setRotation(RightarmUF, 0F, 0F, 0F);
      LeftarmLB = new ModelRenderer(this, 6, 36);
      LeftarmLB.addBox(-1F, -1F, -1F, 4, 2, 2);
      LeftarmLB.setRotationPoint(5F, 17F, 2F);
      LeftarmLB.setTextureSize(61, 53);
      LeftarmLB.mirror = true;
      setRotation(LeftarmLB, 0F, 0F, 0F);
      LeftarmUB = new ModelRenderer(this, 6, 40);
      LeftarmUB.addBox(-1F, -1F, -1F, 4, 2, 2);
      LeftarmUB.setRotationPoint(5F, 13F, 2F);
      LeftarmUB.setTextureSize(61, 53);
      LeftarmUB.mirror = true;
      setRotation(LeftarmUB, 0F, 0F, 0F);
      LeftarmLF = new ModelRenderer(this, 6, 33);
      LeftarmLF.addBox(-1F, -1F, -1F, 4, 2, 2);
      LeftarmLF.setRotationPoint(5F, 17F, -2F);
      LeftarmLF.setTextureSize(61, 53);
      LeftarmLF.mirror = true;
      setRotation(LeftarmLF, 0F, 0F, 0F);
      LeftarmUF = new ModelRenderer(this, 6, 43);
      LeftarmUF.addBox(-1F, -1F, -1F, 4, 2, 2);
      LeftarmUF.setRotationPoint(5F, 13F, -2F);
      LeftarmUF.setTextureSize(61, 53);
      LeftarmUF.mirror = true;
      setRotation(LeftarmUF, 0F, 0F, 0F);
      Neck = new ModelRenderer(this, 45, 14);
      Neck.addBox(-2F, -2F, -2F, 4, 3, 4);
      Neck.setRotationPoint(0.5F, 15.03333F, -5F);
      Neck.setTextureSize(61, 53);
      Neck.mirror = true;
      setRotation(Neck, 0F, 0F, 0F);
      Taillast = new ModelRenderer(this, 0, 29);
      Taillast.addBox(-1F, -1F, 0F, 2, 2, 3);
      Taillast.setRotationPoint(0.5F, 17F, 15F);
      Taillast.setTextureSize(61, 53);
      Taillast.mirror = true;
      setRotation(Taillast, 0F, 0F, 0F);
      Spikebody = new ModelRenderer(this, 0, 45);
      Spikebody.addBox(0F, 0F, 0F, 1, 2, 2);
      Spikebody.setRotationPoint(0F, 10F, -3F);
      Spikebody.setTextureSize(61, 53);
      Spikebody.mirror = true;
      setRotation(Spikebody, 0F, 0F, 0F);
      Spikebodyback = new ModelRenderer(this, 0, 41);
      Spikebodyback.addBox(0F, 0F, 0F, 1, 2, 2);
      Spikebodyback.setRotationPoint(0F, 10F, 1F);
      Spikebodyback.setTextureSize(61, 53);
      Spikebodyback.mirror = true;
      setRotation(Spikebodyback, 0F, 0F, 0F);
      SpikeTail = new ModelRenderer(this, 0, 37);
      SpikeTail.addBox(0F, 0F, 0F, 1, 2, 2);
      SpikeTail.setRotationPoint(0F, 11F, 5F);
      SpikeTail.setTextureSize(61, 53);
      SpikeTail.mirror = true;
      setRotation(SpikeTail, 0F, 0F, 0F);
      SpikeTail1 = new ModelRenderer(this, 0, 33);
      SpikeTail1.addBox(0F, 0F, 0F, 1, 2, 2);
      SpikeTail1.setRotationPoint(0F, 12F, 9F);
      SpikeTail1.setTextureSize(61, 53);
      SpikeTail1.mirror = true;
      setRotation(SpikeTail1, 0F, 0F, 0F);
      Spiketail2 = new ModelRenderer(this, 5, 50);
      Spiketail2.addBox(0F, 0F, 0F, 1, 1, 2);
      Spiketail2.setRotationPoint(0F, 14F, 12.5F);
      Spiketail2.setTextureSize(61, 53);
      Spiketail2.mirror = true;
      setRotation(Spiketail2, 0F, 0F, 0F);
      Spikehead = new ModelRenderer(this, 0, 49);
      Spikehead.addBox(0F, 0F, 0F, 1, 2, 2);
      Spikehead.setRotationPoint(0F, 10F, -9F);
      Spikehead.setTextureSize(61, 53);
      Spikehead.mirror = true;
      setRotation(Spikehead, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Body.render(f5);
    Backbody.render(f5);
    tail.render(f5);
    tail2.render(f5);
    RightlegB.render(f5);
    LeftlegB.render(f5);
    RightlegF.render(f5);
    LeftlegF.render(f5);
    Head.render(f5);
    Nose.render(f5);
    RightarmLB.render(f5);
    RightarmUB.render(f5);
    RightarmLF.render(f5);
    RightarmUF.render(f5);
    LeftarmLB.render(f5);
    LeftarmUB.render(f5);
    LeftarmLF.render(f5);
    LeftarmUF.render(f5);
    Neck.render(f5);
    Taillast.render(f5);
    Spikebody.render(f5);
    Spikebodyback.render(f5);
    SpikeTail.render(f5);
    SpikeTail1.render(f5);
    Spiketail2.render(f5);
    Spikehead.render(f5);
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
  
  /**
   * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
   * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
   * "far" arms and legs can swing at most.
   */
  public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
  {     
      //this.Body.rotateAngleX = ((float)Math.PI / 2F);
      this.RightlegB.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
      this.LeftlegF.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
      this.LeftlegB.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
      this.RightlegF.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
      this.Neck.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
      this.Head.rotateAngleX = this.Neck.rotateAngleX;
      this.Head.rotateAngleY = this.Neck.rotateAngleY;
      this.Nose.rotateAngleY = this.Head.rotateAngleY;
      this.Nose.rotateAngleX = this.Head.rotateAngleX;
      this.Spikehead.rotateAngleY = this.Head.rotateAngleY;
      this.Spikehead.rotateAngleX = this.Head.rotateAngleX;
      this.tail.rotateAngleX = this.Body.rotateAngleX;
      this.SpikeTail.rotateAngleY = this.tail.rotateAngleY;
      this.SpikeTail.rotateAngleX = this.tail.rotateAngleX;
      this.tail2.rotateAngleY = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_; 
      this.SpikeTail1.rotateAngleY = this.tail2.rotateAngleY;
      this.SpikeTail1.rotateAngleX = this.tail2.rotateAngleX;
      this.Taillast.rotateAngleY = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
      this.Spiketail2.rotateAngleY = this.Taillast.rotateAngleY;
      this.Spiketail2.rotateAngleX = this.Taillast.rotateAngleX;
  }
}