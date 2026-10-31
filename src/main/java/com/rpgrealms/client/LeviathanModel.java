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
 * Modelo del Leviatán adulto: como la cría, más anzuelo luminoso, dientes, branquias, espinas dorsales y barbas bioluminiscentes.
 * (GENERADO a partir de una definición única, junto con las texturas.) El frente apunta hacia -Z.
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
    private final ModelPart lure1;
    private final ModelPart lure2;
    private final ModelPart lure3;
    private final ModelPart whisk_l1;
    private final ModelPart whisk_l2;
    private final ModelPart whisk_r1;
    private final ModelPart whisk_r2;
    private final ModelPart spine1;
    private final ModelPart spine2;
    private final ModelPart spine3;
    private final ModelPart spine4;
    private final ModelPart spine5;
    private final ModelPart spine6;
    private final ModelPart gill_l1;
    private final ModelPart gill_l2;
    private final ModelPart gill_l3;
    private final ModelPart gill_r1;
    private final ModelPart gill_r2;
    private final ModelPart gill_r3;

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
        ModelPart lure1 = head.getChild("lure1");
        ModelPart lure2 = lure1.getChild("lure2");
        ModelPart lure3 = lure2.getChild("lure3");
        ModelPart lure_bulb = lure3.getChild("lure_bulb");
        ModelPart spine1 = body.getChild("spine1");
        ModelPart spine2 = body.getChild("spine2");
        ModelPart spine3 = rear.getChild("spine3");
        ModelPart spine4 = rear.getChild("spine4");
        ModelPart spine5 = tail1.getChild("spine5");
        ModelPart spine6 = tail2.getChild("spine6");
        ModelPart gill_l1 = head.getChild("gill_l1");
        ModelPart gill_r1 = head.getChild("gill_r1");
        ModelPart gill_l2 = head.getChild("gill_l2");
        ModelPart gill_r2 = head.getChild("gill_r2");
        ModelPart gill_l3 = head.getChild("gill_l3");
        ModelPart gill_r3 = head.getChild("gill_r3");
        ModelPart tooth_l1 = jaw.getChild("tooth_l1");
        ModelPart tooth_r1 = jaw.getChild("tooth_r1");
        ModelPart tooth_l2 = jaw.getChild("tooth_l2");
        ModelPart tooth_r2 = jaw.getChild("tooth_r2");
        ModelPart tooth_l3 = jaw.getChild("tooth_l3");
        ModelPart tooth_r3 = jaw.getChild("tooth_r3");
        ModelPart tooth_l4 = jaw.getChild("tooth_l4");
        ModelPart tooth_r4 = jaw.getChild("tooth_r4");
        ModelPart toothu_l1 = snout1.getChild("toothu_l1");
        ModelPart toothu_r1 = snout1.getChild("toothu_r1");
        ModelPart toothu_l2 = snout1.getChild("toothu_l2");
        ModelPart toothu_r2 = snout1.getChild("toothu_r2");
        ModelPart toothu_l3 = snout1.getChild("toothu_l3");
        ModelPart toothu_r3 = snout1.getChild("toothu_r3");
        ModelPart whisk_l1 = jaw.getChild("whisk_l1");
        ModelPart whisk_r1 = jaw.getChild("whisk_r1");
        ModelPart whisk_l2 = whisk_l1.getChild("whisk_l2");
        ModelPart whisk_r2 = whisk_r1.getChild("whisk_r2");
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
        this.lure1 = lure1;
        this.lure2 = lure2;
        this.lure3 = lure3;
        this.whisk_l1 = whisk_l1;
        this.whisk_l2 = whisk_l2;
        this.whisk_r1 = whisk_r1;
        this.whisk_r2 = whisk_r2;
        this.spine1 = spine1;
        this.spine2 = spine2;
        this.spine3 = spine3;
        this.spine4 = spine4;
        this.spine5 = spine5;
        this.spine6 = spine6;
        this.gill_l1 = gill_l1;
        this.gill_l2 = gill_l2;
        this.gill_l3 = gill_l3;
        this.gill_r1 = gill_r1;
        this.gill_r2 = gill_r2;
        this.gill_r3 = gill_r3;
        for (ModelPart p : new ModelPart[]{body, mid, rear, tail1, tail2, tail3, fluke, head, jaw, jaw_tip, crest, crest_tip, horn_l, horn_r, dorsal1, dorsal2, dorsal3, dorsal4, ventral1, ventral2, ventral3, pec_l1, pec_l2, pec_l3, pec_r1, pec_r2, pec_r3, wing_l1, wing_l2, wing_r1, wing_r2, fl_l1, fl_l2, fl_l3, fl_r1, fl_r2, fl_r3, tailspike, tailkeel, lure1, lure2, lure3, whisk_l1, whisk_l2, whisk_r1, whisk_r2, spine1, spine2, spine3, spine4, spine5, spine6, gill_l1, gill_l2, gill_l3, gill_r1, gill_r2, gill_r3}) {
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
                CubeListBuilder.create().texOffs(32, 99).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, -8.0F));
        PartDefinition v_jaw = v_head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(48, 38).addBox(-5.0F, 0.0F, -22.0F, 10.0F, 4.0F, 22.0F),
                PartPose.offset(0.0F, 4.0F, -1.0F));
        PartDefinition v_jaw_tip = v_jaw.addOrReplaceChild("jaw_tip",
                CubeListBuilder.create().texOffs(126, 85).addBox(-3.0F, 0.0F, -9.0F, 6.0F, 3.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, -22.0F));
        PartDefinition v_eye_l_big = v_head.addOrReplaceChild("eye_l_big",
                CubeListBuilder.create().texOffs(148, 122).addBox(0.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F),
                PartPose.offset(8.0F, -1.0F, -6.0F));
        PartDefinition v_eye_r_big = v_head.addOrReplaceChild("eye_r_big",
                CubeListBuilder.create().texOffs(160, 122).addBox(-2.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F),
                PartPose.offset(-8.0F, -1.0F, -6.0F));
        PartDefinition v_eye_l_a = v_head.addOrReplaceChild("eye_l_a",
                CubeListBuilder.create().texOffs(108, 132).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(8.0F, -4.0F, -2.0F));
        PartDefinition v_eye_r_a = v_head.addOrReplaceChild("eye_r_a",
                CubeListBuilder.create().texOffs(116, 132).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-8.0F, -4.0F, -2.0F));
        PartDefinition v_eye_l_b = v_head.addOrReplaceChild("eye_l_b",
                CubeListBuilder.create().texOffs(124, 132).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(8.0F, 2.0F, -2.0F));
        PartDefinition v_eye_r_b = v_head.addOrReplaceChild("eye_r_b",
                CubeListBuilder.create().texOffs(132, 132).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-8.0F, 2.0F, -2.0F));
        PartDefinition v_eye_l_c = v_snout1.addOrReplaceChild("eye_l_c",
                CubeListBuilder.create().texOffs(140, 132).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(6.0F, -1.0F, -4.0F));
        PartDefinition v_eye_r_c = v_snout1.addOrReplaceChild("eye_r_c",
                CubeListBuilder.create().texOffs(148, 132).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
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
                CubeListBuilder.create().texOffs(184, 111).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 5.0F),
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
                CubeListBuilder.create().texOffs(198, 111).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 2.0F, 0.4F, 0.0F, 0.0F));
        PartDefinition v_pec_l1 = v_body.addOrReplaceChild("pec_l1",
                CubeListBuilder.create().texOffs(20, 85).addBox(0.0F, -1.0F, -4.0F, 10.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(8.0F, 6.0F, -3.0F, 0.0F, -0.5F, 0.75F));
        PartDefinition v_pec_r1 = v_body.addOrReplaceChild("pec_r1",
                CubeListBuilder.create().texOffs(64, 85).addBox(-10.0F, -1.0F, -4.0F, 10.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(-8.0F, 6.0F, -3.0F, 0.0F, 0.5F, -0.75F));
        PartDefinition v_pec_l2 = v_pec_l1.addOrReplaceChild("pec_l2",
                CubeListBuilder.create().texOffs(56, 99).addBox(0.0F, 0.0F, -3.0F, 10.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, -0.45F, 0.15F));
        PartDefinition v_pec_r2 = v_pec_r1.addOrReplaceChild("pec_r2",
                CubeListBuilder.create().texOffs(96, 99).addBox(-10.0F, 0.0F, -3.0F, 10.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.45F, -0.15F));
        PartDefinition v_pec_l3 = v_pec_l2.addOrReplaceChild("pec_l3",
                CubeListBuilder.create().texOffs(172, 122).addBox(0.0F, 0.0F, -2.0F, 8.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, -0.4F, 0.1F));
        PartDefinition v_pec_r3 = v_pec_r2.addOrReplaceChild("pec_r3",
                CubeListBuilder.create().texOffs(200, 122).addBox(-8.0F, 0.0F, -2.0F, 8.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.4F, -0.1F));
        PartDefinition v_wing_l1 = v_body.addOrReplaceChild("wing_l1",
                CubeListBuilder.create().texOffs(176, 85).addBox(0.0F, -1.0F, -3.0F, 9.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(8.0F, -4.0F, -1.0F, 0.0F, -0.7F, -0.8F));
        PartDefinition v_wing_r1 = v_body.addOrReplaceChild("wing_r1",
                CubeListBuilder.create().texOffs(214, 85).addBox(-9.0F, -1.0F, -3.0F, 9.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -4.0F, -1.0F, 0.0F, 0.7F, 0.8F));
        PartDefinition v_wing_l2 = v_wing_l1.addOrReplaceChild("wing_l2",
                CubeListBuilder.create().texOffs(8, 122).addBox(0.0F, 0.0F, -2.0F, 9.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(9.0F, 0.0F, 0.0F, 0.0F, -0.5F, -0.2F));
        PartDefinition v_wing_r2 = v_wing_r1.addOrReplaceChild("wing_r2",
                CubeListBuilder.create().texOffs(42, 122).addBox(-9.0F, 0.0F, -2.0F, 9.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-9.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.2F));
        PartDefinition v_fluke = v_tail3.addOrReplaceChild("fluke",
                CubeListBuilder.create().texOffs(212, 111).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 0.0F, 12.0F));
        PartDefinition v_fl_l1 = v_fluke.addOrReplaceChild("fl_l1",
                CubeListBuilder.create().texOffs(136, 99).addBox(0.0F, -1.0F, -2.0F, 12.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(3.0F, 0.0F, 2.0F, 0.0F, -0.55F, 0.0F));
        PartDefinition v_fl_r1 = v_fluke.addOrReplaceChild("fl_r1",
                CubeListBuilder.create().texOffs(178, 99).addBox(-12.0F, -1.0F, -2.0F, 12.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(-3.0F, 0.0F, 2.0F, 0.0F, 0.55F, 0.0F));
        PartDefinition v_fl_l2 = v_fl_l1.addOrReplaceChild("fl_l2",
                CubeListBuilder.create().texOffs(76, 122).addBox(0.0F, 0.0F, -1.0F, 10.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(12.0F, 0.0F, 0.0F, 0.0F, -0.5F, 0.0F));
        PartDefinition v_fl_r2 = v_fl_r1.addOrReplaceChild("fl_r2",
                CubeListBuilder.create().texOffs(112, 122).addBox(-10.0F, 0.0F, -1.0F, 10.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-12.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.0F));
        PartDefinition v_fl_l3 = v_fl_l2.addOrReplaceChild("fl_l3",
                CubeListBuilder.create().texOffs(228, 122).addBox(0.0F, 0.0F, -1.0F, 7.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, -0.4F, 0.0F));
        PartDefinition v_fl_r3 = v_fl_r2.addOrReplaceChild("fl_r3",
                CubeListBuilder.create().texOffs(0, 132).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.4F, 0.0F));
        PartDefinition v_lure1 = v_head.addOrReplaceChild("lure1",
                CubeListBuilder.create().texOffs(0, 99).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -9.0F, 0.6F, 0.0F, 0.0F));
        PartDefinition v_lure2 = v_lure1.addOrReplaceChild("lure2",
                CubeListBuilder.create().texOffs(220, 99).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.5F, 0.0F, 0.0F));
        PartDefinition v_lure3 = v_lure2.addOrReplaceChild("lure3",
                CubeListBuilder.create().texOffs(240, 111).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.9F, 0.0F, 0.0F));
        PartDefinition v_lure_bulb = v_lure3.addOrReplaceChild("lure_bulb",
                CubeListBuilder.create().texOffs(8, 99).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -8.0F, 0.0F));
        PartDefinition v_spine1 = v_body.addOrReplaceChild("spine1",
                CubeListBuilder.create().texOffs(228, 99).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, -6.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_spine2 = v_body.addOrReplaceChild("spine2",
                CubeListBuilder.create().texOffs(240, 99).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, 3.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_spine3 = v_rear.addOrReplaceChild("spine3",
                CubeListBuilder.create().texOffs(0, 111).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 3.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_spine4 = v_rear.addOrReplaceChild("spine4",
                CubeListBuilder.create().texOffs(12, 111).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 10.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_spine5 = v_tail1.addOrReplaceChild("spine5",
                CubeListBuilder.create().texOffs(24, 111).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, 5.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_spine6 = v_tail2.addOrReplaceChild("spine6",
                CubeListBuilder.create().texOffs(36, 111).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, 6.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_gill_l1 = v_head.addOrReplaceChild("gill_l1",
                CubeListBuilder.create().texOffs(48, 111).addBox(0.0F, -2.0F, 0.0F, 2.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(8.0F, -4.0F, -1.0F, 0.0F, -0.7F, 0.15F));
        PartDefinition v_gill_r1 = v_head.addOrReplaceChild("gill_r1",
                CubeListBuilder.create().texOffs(68, 111).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, -4.0F, -1.0F, 0.0F, 0.7F, -0.15F));
        PartDefinition v_gill_l2 = v_head.addOrReplaceChild("gill_l2",
                CubeListBuilder.create().texOffs(88, 111).addBox(0.0F, -2.0F, 0.0F, 2.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(8.0F, 0.0F, -1.0F, 0.0F, -0.7F, 0.15F));
        PartDefinition v_gill_r2 = v_head.addOrReplaceChild("gill_r2",
                CubeListBuilder.create().texOffs(108, 111).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, 0.0F, -1.0F, 0.0F, 0.7F, -0.15F));
        PartDefinition v_gill_l3 = v_head.addOrReplaceChild("gill_l3",
                CubeListBuilder.create().texOffs(128, 111).addBox(0.0F, -2.0F, 0.0F, 2.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(8.0F, 4.0F, -1.0F, 0.0F, -0.7F, 0.15F));
        PartDefinition v_gill_r3 = v_head.addOrReplaceChild("gill_r3",
                CubeListBuilder.create().texOffs(148, 111).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, 4.0F, -1.0F, 0.0F, 0.7F, -0.15F));
        PartDefinition v_tooth_l1 = v_jaw.addOrReplaceChild("tooth_l1",
                CubeListBuilder.create().texOffs(24, 132).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(4.0F, 0.0F, -3.0F));
        PartDefinition v_tooth_r1 = v_jaw.addOrReplaceChild("tooth_r1",
                CubeListBuilder.create().texOffs(30, 132).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-4.0F, 0.0F, -3.0F));
        PartDefinition v_tooth_l2 = v_jaw.addOrReplaceChild("tooth_l2",
                CubeListBuilder.create().texOffs(36, 132).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(4.0F, 0.0F, -8.0F));
        PartDefinition v_tooth_r2 = v_jaw.addOrReplaceChild("tooth_r2",
                CubeListBuilder.create().texOffs(42, 132).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-4.0F, 0.0F, -8.0F));
        PartDefinition v_tooth_l3 = v_jaw.addOrReplaceChild("tooth_l3",
                CubeListBuilder.create().texOffs(48, 132).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(4.0F, 0.0F, -13.0F));
        PartDefinition v_tooth_r3 = v_jaw.addOrReplaceChild("tooth_r3",
                CubeListBuilder.create().texOffs(54, 132).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-4.0F, 0.0F, -13.0F));
        PartDefinition v_tooth_l4 = v_jaw.addOrReplaceChild("tooth_l4",
                CubeListBuilder.create().texOffs(60, 132).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(4.0F, 0.0F, -18.0F));
        PartDefinition v_tooth_r4 = v_jaw.addOrReplaceChild("tooth_r4",
                CubeListBuilder.create().texOffs(66, 132).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-4.0F, 0.0F, -18.0F));
        PartDefinition v_toothu_l1 = v_snout1.addOrReplaceChild("toothu_l1",
                CubeListBuilder.create().texOffs(72, 132).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(5.0F, 4.0F, -2.0F));
        PartDefinition v_toothu_r1 = v_snout1.addOrReplaceChild("toothu_r1",
                CubeListBuilder.create().texOffs(78, 132).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-5.0F, 4.0F, -2.0F));
        PartDefinition v_toothu_l2 = v_snout1.addOrReplaceChild("toothu_l2",
                CubeListBuilder.create().texOffs(84, 132).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(5.0F, 4.0F, -5.0F));
        PartDefinition v_toothu_r2 = v_snout1.addOrReplaceChild("toothu_r2",
                CubeListBuilder.create().texOffs(90, 132).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-5.0F, 4.0F, -5.0F));
        PartDefinition v_toothu_l3 = v_snout1.addOrReplaceChild("toothu_l3",
                CubeListBuilder.create().texOffs(96, 132).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(5.0F, 4.0F, -8.0F));
        PartDefinition v_toothu_r3 = v_snout1.addOrReplaceChild("toothu_r3",
                CubeListBuilder.create().texOffs(102, 132).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(-5.0F, 4.0F, -8.0F));
        PartDefinition v_whisk_l1 = v_jaw.addOrReplaceChild("whisk_l1",
                CubeListBuilder.create().texOffs(168, 111).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 3.0F, -14.0F, 0.25F, 0.0F, 0.25F));
        PartDefinition v_whisk_r1 = v_jaw.addOrReplaceChild("whisk_r1",
                CubeListBuilder.create().texOffs(176, 111).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 3.0F, -14.0F, 0.25F, 0.0F, -0.25F));
        PartDefinition v_whisk_l2 = v_whisk_l1.addOrReplaceChild("whisk_l2",
                CubeListBuilder.create().texOffs(248, 111).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.3F, 0.0F, 0.2F));
        PartDefinition v_whisk_r2 = v_whisk_r1.addOrReplaceChild("whisk_r2",
                CubeListBuilder.create().texOffs(0, 122).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.3F, 0.0F, -0.2F));

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
        int atk = entity.getPhase();
        float move = Mth.clamp(limbSwingAmount * 2.0F, 0.0F, 1.0F);

        // Fase de la onda que recorre el cuerpo: más rápida y amplia cuanto más rápido nada.
        float phase = ageInTicks * 0.035F + limbSwing * 0.25F;
        float amp = 0.8F + move * 1.0F + (angry ? 0.5F : 0.0F);

        // Mordida: la animación de ataque de la entidad abre y cierra la mandíbula.
        float bite = this.attackTime > 0.0F ? Mth.sin(this.attackTime * (float) Math.PI) : 0.0F;
        // Boca abierta según el ataque a distancia (orbes / carga / rayo).
        float mouth = atk == 1 ? 0.55F : atk == 2 ? 0.80F : atk == 3 ? 1.00F : 0.0F;

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

        // ---- Cabeza y mandíbula ----
        this.pose(this.head,
                Mth.sin(phase - 0.2F) * 0.03F * amp + headPitch * deg * 0.3F + bite * 0.12F - (atk == 3 ? 0.12F : 0.0F),
                netHeadYaw * deg * 0.35F, 0.0F);
        this.pose(this.jaw,
                0.06F + Mth.sin(ageInTicks * 0.07F) * 0.04F + (angry ? 0.15F : 0.0F) + bite * 0.85F + mouth * 0.75F
                        + (atk >= 2 ? Mth.sin(ageInTicks * 1.7F) * 0.03F : 0.0F),
                0.0F, 0.0F);
        this.pose(this.jaw_tip, bite * 0.2F + mouth * 0.15F, 0.0F, 0.0F);

        // Cresta: se mece; al enfadarse se levanta
        float crestUp = (angry || atk > 0) ? 0.3F : 0.0F;
        this.pose(this.crest, Mth.sin(phase - 0.3F) * 0.04F * amp + crestUp, 0.0F, 0.0F);
        this.pose(this.crest_tip, Mth.sin(phase - 0.8F) * 0.06F * amp + crestUp * 0.6F, 0.0F, 0.0F);
        this.pose(this.horn_l, crestUp * 0.5F, 0.0F, crestUp > 0.0F ? 0.25F : 0.0F);
        this.pose(this.horn_r, crestUp * 0.5F, 0.0F, crestUp > 0.0F ? -0.25F : 0.0F);

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
        float flare = (angry || atk > 0) ? 0.3F : 0.0F;
        float lift = (angry || atk > 0) ? -0.25F : 0.0F;
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
        float w1 = Mth.sin(phase + 1.0F) * 0.10F * amp + ((angry || atk > 0) ? 0.2F : 0.0F);
        float w2 = Mth.sin(phase + 1.6F) * 0.16F * amp;
        this.pose(this.wing_l1, 0.0F, flare * 0.6F, w1);
        this.pose(this.wing_l2, 0.0F, 0.0F, w2);
        this.pose(this.wing_r1, 0.0F, -flare * 0.6F, -w1);
        this.pose(this.wing_r2, 0.0F, 0.0F, -w2);

        // ---- Extras del adulto ----
        // Anzuelo luminoso: se balancea como un péndulo y se agita al atacar
        float lureAmp = atk > 0 ? 2.0F : 1.0F;
        this.pose(this.lure1, Mth.sin(phase * 1.3F - 0.3F) * 0.06F * lureAmp, Mth.sin(phase * 0.7F) * 0.05F * lureAmp, 0.0F);
        this.pose(this.lure2, Mth.sin(phase * 1.3F - 0.9F) * 0.09F * lureAmp, Mth.sin(phase * 0.7F - 0.5F) * 0.07F * lureAmp, 0.0F);
        this.pose(this.lure3, Mth.sin(phase * 1.3F - 1.5F) * 0.13F * lureAmp, Mth.sin(phase * 0.7F - 1.0F) * 0.10F * lureAmp, 0.0F);

        // Barbas: ondulan colgando de la mandíbula
        float wh1 = Mth.sin(phase * 1.1F + 0.2F) * 0.10F;
        float wh2 = Mth.sin(phase * 1.1F - 0.4F) * 0.16F;
        this.pose(this.whisk_l1, wh1, 0.0F, Mth.sin(phase * 0.8F) * 0.08F);
        this.pose(this.whisk_l2, wh2, 0.0F, Mth.sin(phase * 0.8F - 0.6F) * 0.12F);
        this.pose(this.whisk_r1, wh1, 0.0F, -Mth.sin(phase * 0.8F) * 0.08F);
        this.pose(this.whisk_r2, wh2, 0.0F, -Mth.sin(phase * 0.8F - 0.6F) * 0.12F);

        // Espinas del lomo siguen la onda (y se erizan al atacar)
        float erect = atk > 0 || angry ? -0.2F : 0.0F;
        this.pose(this.spine1, Mth.sin(phase - 0.2F) * 0.05F * amp + erect, 0.0F, 0.0F);
        this.pose(this.spine2, Mth.sin(phase - 0.5F) * 0.05F * amp + erect, 0.0F, 0.0F);
        this.pose(this.spine3, Mth.sin(phase - 1.0F) * 0.06F * amp + erect, 0.0F, 0.0F);
        this.pose(this.spine4, Mth.sin(phase - 1.3F) * 0.07F * amp + erect, 0.0F, 0.0F);
        this.pose(this.spine5, Mth.sin(phase - 1.7F) * 0.08F * amp + erect, 0.0F, 0.0F);
        this.pose(this.spine6, Mth.sin(phase - 2.1F) * 0.09F * amp + erect, 0.0F, 0.0F);

        // Branquias: aletean rápido, como respirando
        float gill = Mth.sin(ageInTicks * 0.25F) * 0.12F + (atk > 0 ? 0.25F : 0.0F);
        this.pose(this.gill_l1, 0.0F, gill, 0.0F);
        this.pose(this.gill_l2, 0.0F, gill * 0.9F, 0.0F);
        this.pose(this.gill_l3, 0.0F, gill * 0.8F, 0.0F);
        this.pose(this.gill_r1, 0.0F, -gill, 0.0F);
        this.pose(this.gill_r2, 0.0F, -gill * 0.9F, 0.0F);
        this.pose(this.gill_r3, 0.0F, -gill * 0.8F, 0.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
