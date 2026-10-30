package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo del Leviatán (GENERADO a partir de una definición única, junto con la textura).
 * Hocico largo, cresta, ojos en fila, aleta dorsal escalonada, aletas pectorales y laterales,
 * aleta ventral y cola con aleta en media luna. El frente apunta hacia -Z.
 */
public class LeviathanModel extends EntityModel<LeviathanEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(RpgRealms.MODID, "leviathan"), "main");

    private final ModelPart body;
    private final ModelPart mid;
    private final ModelPart rear;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart fluke;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart jaw_tip;
    private final ModelPart crest;
    private final ModelPart crest_tip;
    private final ModelPart horn_l;
    private final ModelPart horn_r;
    private final ModelPart dorsal1;
    private final ModelPart dorsal2;
    private final ModelPart dorsal3;
    private final ModelPart dorsal4;
    private final ModelPart ventral1;
    private final ModelPart ventral2;
    private final ModelPart ventral3;
    private final ModelPart pec_l1;
    private final ModelPart pec_l2;
    private final ModelPart pec_l3;
    private final ModelPart pec_r1;
    private final ModelPart pec_r2;
    private final ModelPart pec_r3;
    private final ModelPart wing_l1;
    private final ModelPart wing_l2;
    private final ModelPart wing_r1;
    private final ModelPart wing_r2;
    private final ModelPart fl_l1;
    private final ModelPart fl_l2;
    private final ModelPart fl_l3;
    private final ModelPart fl_r1;
    private final ModelPart fl_r2;
    private final ModelPart fl_r3;
    private final ModelPart tailspike;
    private final ModelPart tailkeel;

    /** Pose de reposo de cada parte animada: {rotX, rotY, rotZ, y}. */
    private final Map<ModelPart, float[]> rest = new HashMap<>();

    public LeviathanModel(ModelPart root) {
        ModelPart body = root.getChild("body");
        ModelPart mid = body.getChild("mid");
        ModelPart rear = mid.getChild("rear");
        ModelPart tail1 = rear.getChild("tail1");
        ModelPart tail2 = tail1.getChild("tail2");
        ModelPart tail3 = tail2.getChild("tail3");
        ModelPart head = body.getChild("head");
        ModelPart snout1 = head.getChild("snout1");
        ModelPart snout2 = snout1.getChild("snout2");
        ModelPart snout3 = snout2.getChild("snout3");
        ModelPart jaw = head.getChild("jaw");
        ModelPart jaw_tip = jaw.getChild("jaw_tip");
        ModelPart eye_l_big = head.getChild("eye_l_big");
        ModelPart eye_r_big = head.getChild("eye_r_big");
        ModelPart eye_l_a = head.getChild("eye_l_a");
        ModelPart eye_r_a = head.getChild("eye_r_a");
        ModelPart eye_l_b = head.getChild("eye_l_b");
        ModelPart eye_r_b = head.getChild("eye_r_b");
        ModelPart eye_l_c = snout1.getChild("eye_l_c");
        ModelPart eye_r_c = snout1.getChild("eye_r_c");
        ModelPart crest = head.getChild("crest");
        ModelPart crest_tip = crest.getChild("crest_tip");
        ModelPart horn_l = head.getChild("horn_l");
        ModelPart horn_r = head.getChild("horn_r");
        ModelPart dorsal1 = mid.getChild("dorsal1");
        ModelPart dorsal2 = dorsal1.getChild("dorsal2");
        ModelPart dorsal3 = dorsal2.getChild("dorsal3");
        ModelPart dorsal4 = dorsal3.getChild("dorsal4");
        ModelPart tailspike = tail1.getChild("tailspike");
        ModelPart tailkeel = tail2.getChild("tailkeel");
        ModelPart ventral1 = rear.getChild("ventral1");
        ModelPart ventral2 = ventral1.getChild("ventral2");
        ModelPart ventral3 = ventral2.getChild("ventral3");
        ModelPart pec_l1 = body.getChild("pec_l1");
        ModelPart pec_r1 = body.getChild("pec_r1");
        ModelPart pec_l2 = pec_l1.getChild("pec_l2");
        ModelPart pec_r2 = pec_r1.getChild("pec_r2");
        ModelPart pec_l3 = pec_l2.getChild("pec_l3");
        ModelPart pec_r3 = pec_r2.getChild("pec_r3");
        ModelPart wing_l1 = body.getChild("wing_l1");
        ModelPart wing_r1 = body.getChild("wing_r1");
        ModelPart wing_l2 = wing_l1.getChild("wing_l2");
        ModelPart wing_r2 = wing_r1.getChild("wing_r2");
        ModelPart fluke = tail3.getChild("fluke");
        ModelPart fl_l1 = fluke.getChild("fl_l1");
        ModelPart fl_r1 = fluke.getChild("fl_r1");
        ModelPart fl_l2 = fl_l1.getChild("fl_l2");
        ModelPart fl_r2 = fl_r1.getChild("fl_r2");
        ModelPart fl_l3 = fl_l2.getChild("fl_l3");
        ModelPart fl_r3 = fl_r2.getChild("fl_r3");
        this.body = body;
        this.mid = mid;
        this.rear = rear;
        this.tail1 = tail1;
        this.tail2 = tail2;
        this.tail3 = tail3;
        this.fluke = fluke;
        this.head = head;
        this.jaw = jaw;
        this.jaw_tip = jaw_tip;
        this.crest = crest;
        this.crest_tip = crest_tip;
        this.horn_l = horn_l;
        this.horn_r = horn_r;
        this.dorsal1 = dorsal1;
        this.dorsal2 = dorsal2;
        this.dorsal3 = dorsal3;
        this.dorsal4 = dorsal4;
        this.ventral1 = ventral1;
        this.ventral2 = ventral2;
        this.ventral3 = ventral3;
        this.pec_l1 = pec_l1;
        this.pec_l2 = pec_l2;
        this.pec_l3 = pec_l3;
        this.pec_r1 = pec_r1;
        this.pec_r2 = pec_r2;
        this.pec_r3 = pec_r3;
        this.wing_l1 = wing_l1;
        this.wing_l2 = wing_l2;
        this.wing_r1 = wing_r1;
        this.wing_r2 = wing_r2;
        this.fl_l1 = fl_l1;
        this.fl_l2 = fl_l2;
        this.fl_l3 = fl_l3;
        this.fl_r1 = fl_r1;
        this.fl_r2 = fl_r2;
        this.fl_r3 = fl_r3;
        this.tailspike = tailspike;
        this.tailkeel = tailkeel;
        for (ModelPart p : new ModelPart[]{body, mid, rear, tail1, tail2, tail3, fluke, head, jaw, jaw_tip, crest, crest_tip, horn_l, horn_r, dorsal1, dorsal2, dorsal3, dorsal4, ventral1, ventral2, ventral3, pec_l1, pec_l2, pec_l3, pec_r1, pec_r2, pec_r3, wing_l1, wing_l2, wing_r1, wing_r2, fl_l1, fl_l2, fl_l3, fl_r1, fl_r2, fl_r3, tailspike, tailkeel}) {
            this.rest.put(p, new float[]{p.xRot, p.yRot, p.zRot, p.y});
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition v_body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -8.0F, -11.0F, 18.0F, 16.0F, 22.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition v_mid = v_body.addOrReplaceChild("mid",
                CubeListBuilder.create().texOffs(80, 0).addBox(-10.0F, -9.0F, 0.0F, 20.0F, 18.0F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 11.0F));
        PartDefinition v_rear = v_mid.addOrReplaceChild("rear",
                CubeListBuilder.create().texOffs(156, 0).addBox(-7.0F, -7.0F, 0.0F, 14.0F, 14.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 18.0F));
        PartDefinition v_tail1 = v_rear.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 38).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 12.0F, 14.0F),
                PartPose.offset(0.0F, 0.0F, 16.0F));
        PartDefinition v_tail2 = v_tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(168, 38).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 14.0F),
                PartPose.offset(0.0F, 0.0F, 14.0F));
        PartDefinition v_tail3 = v_tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(104, 64).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 6.0F, 12.0F),
                PartPose.offset(0.0F, 0.0F, 14.0F));
        PartDefinition v_head = v_body.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(112, 38).addBox(-8.0F, -7.0F, -12.0F, 16.0F, 13.0F, 12.0F),
                PartPose.offset(0.0F, 0.0F, -11.0F));
        PartDefinition v_snout1 = v_head.addOrReplaceChild("snout1",
                CubeListBuilder.create().texOffs(60, 64).addBox(-6.0F, -5.0F, -10.0F, 12.0F, 9.0F, 10.0F),
                PartPose.offset(0.0F, 1.0F, -12.0F));
        PartDefinition v_snout2 = v_snout1.addOrReplaceChild("snout2",
                CubeListBuilder.create().texOffs(206, 64).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 7.0F, 8.0F),
                PartPose.offset(0.0F, 0.0F, -10.0F));
        PartDefinition v_snout3 = v_snout2.addOrReplaceChild("snout3",
                CubeListBuilder.create().texOffs(0, 99).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, -8.0F));
        PartDefinition v_jaw = v_head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(48, 38).addBox(-5.0F, 0.0F, -22.0F, 10.0F, 4.0F, 22.0F),
                PartPose.offset(0.0F, 4.0F, -1.0F));
        PartDefinition v_jaw_tip = v_jaw.addOrReplaceChild("jaw_tip",
                CubeListBuilder.create().texOffs(126, 85).addBox(-3.0F, 0.0F, -9.0F, 6.0F, 3.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, -22.0F));
        PartDefinition v_eye_l_big = v_head.addOrReplaceChild("eye_l_big",
                CubeListBuilder.create().texOffs(140, 110).addBox(0.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F),
                PartPose.offset(8.0F, -1.0F, -6.0F));
        PartDefinition v_eye_r_big = v_head.addOrReplaceChild("eye_r_big",
                CubeListBuilder.create().texOffs(152, 110).addBox(-2.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F),
                PartPose.offset(-8.0F, -1.0F, -6.0F));
        PartDefinition v_eye_l_a = v_head.addOrReplaceChild("eye_l_a",
                CubeListBuilder.create().texOffs(24, 119).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(8.0F, -4.0F, -2.0F));
        PartDefinition v_eye_r_a = v_head.addOrReplaceChild("eye_r_a",
                CubeListBuilder.create().texOffs(32, 119).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-8.0F, -4.0F, -2.0F));
        PartDefinition v_eye_l_b = v_head.addOrReplaceChild("eye_l_b",
                CubeListBuilder.create().texOffs(40, 119).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(8.0F, 2.0F, -2.0F));
        PartDefinition v_eye_r_b = v_head.addOrReplaceChild("eye_r_b",
                CubeListBuilder.create().texOffs(48, 119).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-8.0F, 2.0F, -2.0F));
        PartDefinition v_eye_l_c = v_snout1.addOrReplaceChild("eye_l_c",
                CubeListBuilder.create().texOffs(56, 119).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(6.0F, -1.0F, -4.0F));
        PartDefinition v_eye_r_c = v_snout1.addOrReplaceChild("eye_r_c",
                CubeListBuilder.create().texOffs(64, 119).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-6.0F, -1.0F, -4.0F));
        PartDefinition v_crest = v_head.addOrReplaceChild("crest",
                CubeListBuilder.create().texOffs(212, 38).addBox(-2.0F, -14.0F, -3.0F, 4.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -2.0F, -0.75F, 0.0F, 0.0F));
        PartDefinition v_crest_tip = v_crest.addOrReplaceChild("crest_tip",
                CubeListBuilder.create().texOffs(238, 64).addBox(-1.0F, -10.0F, -2.0F, 2.0F, 10.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -14.0F, 1.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_horn_l = v_head.addOrReplaceChild("horn_l",
                CubeListBuilder.create().texOffs(156, 85).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, -6.0F, -4.0F, -0.8F, 0.0F, 0.35F));
        PartDefinition v_horn_r = v_head.addOrReplaceChild("horn_r",
                CubeListBuilder.create().texOffs(166, 85).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 3.0F),
                PartPose.offsetAndRotation(-6.0F, -6.0F, -4.0F, -0.8F, 0.0F, -0.35F));
        PartDefinition v_dorsal1 = v_mid.addOrReplaceChild("dorsal1",
                CubeListBuilder.create().texOffs(0, 64).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 2.0F, -0.25F, 0.0F, 0.0F));
        PartDefinition v_dorsal2 = v_dorsal1.addOrReplaceChild("dorsal2",
                CubeListBuilder.create().texOffs(136, 64).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 3.0F, -0.3F, 0.0F, 0.0F));
        PartDefinition v_dorsal3 = v_dorsal2.addOrReplaceChild("dorsal3",
                CubeListBuilder.create().texOffs(0, 85).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 3.0F, -0.3F, 0.0F, 0.0F));
        PartDefinition v_dorsal4 = v_dorsal3.addOrReplaceChild("dorsal4",
                CubeListBuilder.create().texOffs(188, 99).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, 2.0F, -0.35F, 0.0F, 0.0F));
        PartDefinition v_tailspike = v_tail1.addOrReplaceChild("tailspike",
                CubeListBuilder.create().texOffs(162, 64).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, -0.7F, 0.0F, 0.0F));
        PartDefinition v_tailkeel = v_tail2.addOrReplaceChild("tailkeel",
                CubeListBuilder.create().texOffs(108, 85).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 6.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, 0.7F, 0.0F, 0.0F));
        PartDefinition v_ventral1 = v_rear.addOrReplaceChild("ventral1",
                CubeListBuilder.create().texOffs(32, 64).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 3.0F, 0.4F, 0.0F, 0.0F));
        PartDefinition v_ventral2 = v_ventral1.addOrReplaceChild("ventral2",
                CubeListBuilder.create().texOffs(184, 64).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 7.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, 0.4F, 0.0F, 0.0F));
        PartDefinition v_ventral3 = v_ventral2.addOrReplaceChild("ventral3",
                CubeListBuilder.create().texOffs(202, 99).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 2.0F, 0.4F, 0.0F, 0.0F));
        PartDefinition v_pec_l1 = v_body.addOrReplaceChild("pec_l1",
                CubeListBuilder.create().texOffs(20, 85).addBox(0.0F, -1.0F, -4.0F, 10.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(8.0F, 6.0F, -3.0F, 0.0F, -0.5F, 0.75F));
        PartDefinition v_pec_r1 = v_body.addOrReplaceChild("pec_r1",
                CubeListBuilder.create().texOffs(64, 85).addBox(-10.0F, -1.0F, -4.0F, 10.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(-8.0F, 6.0F, -3.0F, 0.0F, 0.5F, -0.75F));
        PartDefinition v_pec_l2 = v_pec_l1.addOrReplaceChild("pec_l2",
                CubeListBuilder.create().texOffs(24, 99).addBox(0.0F, 0.0F, -3.0F, 10.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, -0.45F, 0.15F));
        PartDefinition v_pec_r2 = v_pec_r1.addOrReplaceChild("pec_r2",
                CubeListBuilder.create().texOffs(64, 99).addBox(-10.0F, 0.0F, -3.0F, 10.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.45F, -0.15F));
        PartDefinition v_pec_l3 = v_pec_l2.addOrReplaceChild("pec_l3",
                CubeListBuilder.create().texOffs(164, 110).addBox(0.0F, 0.0F, -2.0F, 8.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, -0.4F, 0.1F));
        PartDefinition v_pec_r3 = v_pec_r2.addOrReplaceChild("pec_r3",
                CubeListBuilder.create().texOffs(192, 110).addBox(-8.0F, 0.0F, -2.0F, 8.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.4F, -0.1F));
        PartDefinition v_wing_l1 = v_body.addOrReplaceChild("wing_l1",
                CubeListBuilder.create().texOffs(176, 85).addBox(0.0F, -1.0F, -3.0F, 9.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(8.0F, -4.0F, -1.0F, 0.0F, -0.7F, -0.8F));
        PartDefinition v_wing_r1 = v_body.addOrReplaceChild("wing_r1",
                CubeListBuilder.create().texOffs(214, 85).addBox(-9.0F, -1.0F, -3.0F, 9.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -4.0F, -1.0F, 0.0F, 0.7F, 0.8F));
        PartDefinition v_wing_l2 = v_wing_l1.addOrReplaceChild("wing_l2",
                CubeListBuilder.create().texOffs(0, 110).addBox(0.0F, 0.0F, -2.0F, 9.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(9.0F, 0.0F, 0.0F, 0.0F, -0.5F, -0.2F));
        PartDefinition v_wing_r2 = v_wing_r1.addOrReplaceChild("wing_r2",
                CubeListBuilder.create().texOffs(34, 110).addBox(-9.0F, 0.0F, -2.0F, 9.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-9.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.2F));
        PartDefinition v_fluke = v_tail3.addOrReplaceChild("fluke",
                CubeListBuilder.create().texOffs(216, 99).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 0.0F, 12.0F));
        PartDefinition v_fl_l1 = v_fluke.addOrReplaceChild("fl_l1",
                CubeListBuilder.create().texOffs(104, 99).addBox(0.0F, -1.0F, -2.0F, 12.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(3.0F, 0.0F, 2.0F, 0.0F, -0.55F, 0.0F));
        PartDefinition v_fl_r1 = v_fluke.addOrReplaceChild("fl_r1",
                CubeListBuilder.create().texOffs(146, 99).addBox(-12.0F, -1.0F, -2.0F, 12.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(-3.0F, 0.0F, 2.0F, 0.0F, 0.55F, 0.0F));
        PartDefinition v_fl_l2 = v_fl_l1.addOrReplaceChild("fl_l2",
                CubeListBuilder.create().texOffs(68, 110).addBox(0.0F, 0.0F, -1.0F, 10.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(12.0F, 0.0F, 0.0F, 0.0F, -0.5F, 0.0F));
        PartDefinition v_fl_r2 = v_fl_r1.addOrReplaceChild("fl_r2",
                CubeListBuilder.create().texOffs(104, 110).addBox(-10.0F, 0.0F, -1.0F, 10.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-12.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.0F));
        PartDefinition v_fl_l3 = v_fl_l2.addOrReplaceChild("fl_l3",
                CubeListBuilder.create().texOffs(220, 110).addBox(0.0F, 0.0F, -1.0F, 7.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, -0.4F, 0.0F));
        PartDefinition v_fl_r3 = v_fl_r2.addOrReplaceChild("fl_r3",
                CubeListBuilder.create().texOffs(0, 119).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.4F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    private void pose(ModelPart part, float dx, float dy, float dz) {
        float[] r = this.rest.get(part);
        part.xRot = r[0] + dx;
        part.yRot = r[1] + dy;
        part.zRot = r[2] + dz;
    }

    @Override
    public void setupAnim(LeviathanEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        final float deg = (float) Math.PI / 180F;
        boolean angry = entity.isAggressive();
        float move = Mth.clamp(limbSwingAmount * 2.0F, 0.0F, 1.0F);

        // Fase de la onda que recorre el cuerpo: más rápida y amplia cuanto más rápido nada.
        float phase = ageInTicks * 0.05F + limbSwing * 0.25F;
        float amp = 0.8F + move * 1.0F + (angry ? 0.5F : 0.0F);

        // Mordida: la animación de ataque de la entidad abre y cierra la mandíbula.
        float bite = this.attackTime > 0.0F ? Mth.sin(this.attackTime * (float) Math.PI) : 0.0F;

        // ---- Cuerpo: balanceo suave + inclinación según hacia donde mira ----
        this.pose(this.body, headPitch * deg * 0.7F + Mth.sin(phase) * 0.02F * amp, 0.0F, 0.0F);
        this.body.y = this.rest.get(this.body)[3] + Mth.sin(ageInTicks * 0.04F) * 0.6F;

        // ---- Onda vertical que se propaga hasta la cola (más amplitud hacia atrás) ----
        this.pose(this.mid,   Mth.sin(phase - 0.5F) * 0.04F * amp, Mth.sin(phase * 0.5F - 0.4F) * 0.03F * amp, 0.0F);
        this.pose(this.rear,  Mth.sin(phase - 1.0F) * 0.07F * amp, Mth.sin(phase * 0.5F - 0.8F) * 0.05F * amp, 0.0F);
        this.pose(this.tail1, Mth.sin(phase - 1.5F) * 0.10F * amp, Mth.sin(phase * 0.5F - 1.2F) * 0.07F * amp, 0.0F);
        this.pose(this.tail2, Mth.sin(phase - 2.0F) * 0.13F * amp, Mth.sin(phase * 0.5F - 1.6F) * 0.09F * amp, 0.0F);
        this.pose(this.tail3, Mth.sin(phase - 2.5F) * 0.16F * amp, Mth.sin(phase * 0.5F - 2.0F) * 0.11F * amp, 0.0F);
        this.pose(this.fluke, Mth.sin(phase - 3.0F) * 0.22F * amp, 0.0F, 0.0F);

        // Alas de la cola: se doblan como una media luna que bate
        float f1 = Mth.sin(phase - 3.4F) * 0.16F * amp;
        float f2 = Mth.sin(phase - 3.9F) * 0.22F * amp;
        float f3 = Mth.sin(phase - 4.4F) * 0.28F * amp;
        this.pose(this.fl_l1, 0.0F, 0.0F, f1);
        this.pose(this.fl_l2, 0.0F, 0.0F, f2);
        this.pose(this.fl_l3, 0.0F, 0.0F, f3);
        this.pose(this.fl_r1, 0.0F, 0.0F, -f1);
        this.pose(this.fl_r2, 0.0F, 0.0F, -f2);
        this.pose(this.fl_r3, 0.0F, 0.0F, -f3);

        // ---- Cabeza ----
        this.pose(this.head,
                Mth.sin(phase - 0.2F) * 0.03F * amp + headPitch * deg * 0.3F + bite * 0.12F,
                netHeadYaw * deg * 0.35F, 0.0F);
        this.pose(this.jaw,
                0.06F + Mth.sin(ageInTicks * 0.07F) * 0.04F + (angry ? 0.15F : 0.0F) + bite * 0.85F,
                0.0F, 0.0F);
        this.pose(this.jaw_tip, bite * 0.2F, 0.0F, 0.0F);

        // Cresta: se mece; al enfadarse se levanta
        float crestUp = angry ? 0.3F : 0.0F;
        this.pose(this.crest, Mth.sin(phase - 0.3F) * 0.04F * amp + crestUp, 0.0F, 0.0F);
        this.pose(this.crest_tip, Mth.sin(phase - 0.8F) * 0.06F * amp + crestUp * 0.6F, 0.0F, 0.0F);
        this.pose(this.horn_l, crestUp * 0.5F, 0.0F, angry ? 0.25F : 0.0F);
        this.pose(this.horn_r, crestUp * 0.5F, 0.0F, angry ? -0.25F : 0.0F);

        // ---- Aleta dorsal y cola: siguen la onda con retraso ----
        this.pose(this.dorsal1, Mth.sin(phase - 1.0F) * 0.03F * amp, 0.0F, 0.0F);
        this.pose(this.dorsal2, Mth.sin(phase - 1.3F) * 0.05F * amp, 0.0F, 0.0F);
        this.pose(this.dorsal3, Mth.sin(phase - 1.6F) * 0.07F * amp, 0.0F, 0.0F);
        this.pose(this.dorsal4, Mth.sin(phase - 1.9F) * 0.09F * amp, 0.0F, 0.0F);
        this.pose(this.tailspike, Mth.sin(phase - 2.2F) * 0.08F * amp, 0.0F, 0.0F);
        this.pose(this.tailkeel, Mth.sin(phase - 2.4F) * 0.08F * amp, 0.0F, 0.0F);
        this.pose(this.ventral1, Mth.sin(phase - 1.1F) * 0.05F * amp, 0.0F, 0.0F);
        this.pose(this.ventral2, Mth.sin(phase - 1.5F) * 0.08F * amp, 0.0F, 0.0F);
        this.pose(this.ventral3, Mth.sin(phase - 1.9F) * 0.11F * amp, 0.0F, 0.0F);

        // ---- Aletas pectorales: remada lenta; al enfadarse se abren ----
        float flare = angry ? 0.3F : 0.0F;      // abren hacia afuera
        float lift = angry ? -0.25F : 0.0F;     // y se levantan
        float p1 = Mth.sin(phase + 0.4F) * 0.14F * amp + lift;
        float p2 = Mth.sin(phase + 0.9F) * 0.20F * amp;
        float p3 = Mth.sin(phase + 1.4F) * 0.26F * amp;
        this.pose(this.pec_l1, 0.0F, flare, p1);
        this.pose(this.pec_l2, 0.0F, flare * 0.5F, p2);
        this.pose(this.pec_l3, 0.0F, flare * 0.3F, p3);
        this.pose(this.pec_r1, 0.0F, -flare, -p1);
        this.pose(this.pec_r2, 0.0F, -flare * 0.5F, -p2);
        this.pose(this.pec_r3, 0.0F, -flare * 0.3F, -p3);

        // ---- Aletas laterales superiores ----
        float w1 = Mth.sin(phase + 1.0F) * 0.10F * amp + (angry ? 0.2F : 0.0F);
        float w2 = Mth.sin(phase + 1.6F) * 0.16F * amp;
        this.pose(this.wing_l1, 0.0F, flare * 0.6F, w1);
        this.pose(this.wing_l2, 0.0F, 0.0F, w2);
        this.pose(this.wing_r1, 0.0F, -flare * 0.6F, -w1);
        this.pose(this.wing_r2, 0.0F, 0.0F, -w2);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
