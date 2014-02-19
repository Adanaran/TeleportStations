package com.dsi11.teleportstations.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * Custom model for teleporter.
 * 
 * @author Demitreus
 */
public class ModelTeleporter extends ModelBase {
	ModelRenderer[] Models = new ModelRenderer[5];

	/**
	 * Creates a new Model.
	 */
	public ModelTeleporter() {
		for (int i = 0; i < 4; i++) {
			Models[i] = new ModelRenderer(this, 0, 0);
		}
		Models[1].addBox(7F, -54F, -8F, 1, 38, 1);
		Models[2].addBox(7F, -54F, 7F, 1, 38, 1);
		Models[3].addBox(-8F, -54F, 7F, 1, 38, 1);
		Models[0].addBox(-8F, -54F, -8F, 1, 38, 1);

		for (int i = 0; i < 4; i++) {
			Models[i].setRotationPoint(0F, 0F, 0F);
			setRotation(Models[i], 0f, 0f, 0f);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void render() {
		for (int i = 0; i < 4; i++) {
			Models[i].render(0.0625f);
		}
	}
}