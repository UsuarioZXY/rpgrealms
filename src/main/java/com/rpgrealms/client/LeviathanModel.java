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
 * Modelo del Leviatán adulto.
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
    private final ModelPart hind_l;
    private final ModelPart hind_r;
    private final ModelPart eyelid_l;
    private final ModelPart eyelid_r;
    private final ModelPart tongue;
    private final ModelPart tail4;
    private final ModelPart brow_l;
    private final ModelPart brow_r;
    private final ModelPart lure1;
    private final ModelPart lure2;
    private final ModelPart lure3;

    /** Pose de reposo de cada parte animada: {rotX, rotY, rotZ, y}. */
    private final Map<ModelPart, float[]> rest = new HashMap<>();

    public LeviathanModel(ModelPart root) {
        ModelPart body = root.getChild("body");
        ModelPart mid = body.getChild("mid");
        ModelPart rear = mid.getChild("rear");
        ModelPart tail1 = rear.getChild("tail1");
        ModelPart tail2 = tail1.getChild("tail2");
        ModelPart tail3 = tail2.getChild("tail3");
        ModelPart tail4 = tail3.getChild("tail4");
        ModelPart head = body.getChild("head");
        ModelPart melon = head.getChild("melon");
        ModelPart snout1 = head.getChild("snout1");
        ModelPart snout2 = snout1.getChild("snout2");
        ModelPart snout3 = snout2.getChild("snout3");
        ModelPart snout4 = snout3.getChild("snout4");
        ModelPart jaw = head.getChild("jaw");
        ModelPart jaw_tip = jaw.getChild("jaw_tip");
        ModelPart wall_l = jaw.getChild("wall_l");
        ModelPart wall_r = jaw.getChild("wall_r");
        ModelPart wallt_l = jaw_tip.getChild("wallt_l");
        ModelPart wallt_r = jaw_tip.getChild("wallt_r");
        ModelPart lip = jaw_tip.getChild("lip");
        ModelPart palate0 = head.getChild("palate0");
        ModelPart palate1 = snout1.getChild("palate1");
        ModelPart palate2 = snout2.getChild("palate2");
        ModelPart palate3 = snout3.getChild("palate3");
        ModelPart palate4 = snout4.getChild("palate4");
        ModelPart cheek_l = head.getChild("cheek_l");
        ModelPart cheek_r = head.getChild("cheek_r");
        ModelPart throat = head.getChild("throat");
        ModelPart eye_l_big = head.getChild("eye_l_big");
        ModelPart eye_r_big = head.getChild("eye_r_big");
        ModelPart eye_l_a = head.getChild("eye_l_a");
        ModelPart eye_r_a = head.getChild("eye_r_a");
        ModelPart eye_l_b = head.getChild("eye_l_b");
        ModelPart eye_r_b = head.getChild("eye_r_b");
        ModelPart eye_l_c = snout1.getChild("eye_l_c");
        ModelPart eye_r_c = snout1.getChild("eye_r_c");
        ModelPart eye_l_d = head.getChild("eye_l_d");
        ModelPart eye_r_d = head.getChild("eye_r_d");
        ModelPart eye_l_e = head.getChild("eye_l_e");
        ModelPart eye_r_e = head.getChild("eye_r_e");
        ModelPart crest = head.getChild("crest");
        ModelPart crest_tip = crest.getChild("crest_tip");
        ModelPart horn_l = head.getChild("horn_l");
        ModelPart horn_r = head.getChild("horn_r");
        ModelPart gill_l = head.getChild("gill_l");
        ModelPart gill_r = head.getChild("gill_r");
        ModelPart dorsal1 = mid.getChild("dorsal1");
        ModelPart dorsal2 = dorsal1.getChild("dorsal2");
        ModelPart dorsal3 = dorsal2.getChild("dorsal3");
        ModelPart dorsal4 = dorsal3.getChild("dorsal4");
        ModelPart dorsal_b = rear.getChild("dorsal_b");
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
        ModelPart hind_l = rear.getChild("hind_l");
        ModelPart hind_r = rear.getChild("hind_r");
        ModelPart wing_l1 = head.getChild("wing_l1");
        ModelPart wing_r1 = head.getChild("wing_r1");
        ModelPart wing_l2 = wing_l1.getChild("wing_l2");
        ModelPart wing_r2 = wing_r1.getChild("wing_r2");
        ModelPart fluke = tail4.getChild("fluke");
        ModelPart fl_l1 = fluke.getChild("fl_l1");
        ModelPart fl_l2 = fl_l1.getChild("fl_l2");
        ModelPart fl_l3 = fl_l2.getChild("fl_l3");
        ModelPart fl_r1 = fluke.getChild("fl_r1");
        ModelPart fl_r2 = fl_r1.getChild("fl_r2");
        ModelPart fl_r3 = fl_r2.getChild("fl_r3");
        ModelPart tongue = jaw.getChild("tongue");
        ModelPart tl_l0 = jaw.getChild("tl_l0");
        ModelPart tl_r0 = jaw.getChild("tl_r0");
        ModelPart tl_l1 = jaw.getChild("tl_l1");
        ModelPart tl_r1 = jaw.getChild("tl_r1");
        ModelPart tl_l2 = jaw.getChild("tl_l2");
        ModelPart tl_r2 = jaw.getChild("tl_r2");
        ModelPart tl_l3 = jaw.getChild("tl_l3");
        ModelPart tl_r3 = jaw.getChild("tl_r3");
        ModelPart tl_l4 = jaw.getChild("tl_l4");
        ModelPart tl_r4 = jaw.getChild("tl_r4");
        ModelPart tl_l5 = jaw.getChild("tl_l5");
        ModelPart tl_r5 = jaw.getChild("tl_r5");
        ModelPart tl_l6 = jaw.getChild("tl_l6");
        ModelPart tl_r6 = jaw.getChild("tl_r6");
        ModelPart tl_l7 = jaw.getChild("tl_l7");
        ModelPart tl_r7 = jaw.getChild("tl_r7");
        ModelPart tl_l8 = jaw.getChild("tl_l8");
        ModelPart tl_r8 = jaw.getChild("tl_r8");
        ModelPart tl_l9 = jaw.getChild("tl_l9");
        ModelPart tl_r9 = jaw.getChild("tl_r9");
        ModelPart tl_l10 = jaw.getChild("tl_l10");
        ModelPart tl_r10 = jaw.getChild("tl_r10");
        ModelPart tt_l0 = jaw_tip.getChild("tt_l0");
        ModelPart tt_r0 = jaw_tip.getChild("tt_r0");
        ModelPart tt_l1 = jaw_tip.getChild("tt_l1");
        ModelPart tt_r1 = jaw_tip.getChild("tt_r1");
        ModelPart tt_l2 = jaw_tip.getChild("tt_l2");
        ModelPart tt_r2 = jaw_tip.getChild("tt_r2");
        ModelPart tt_l3 = jaw_tip.getChild("tt_l3");
        ModelPart tt_r3 = jaw_tip.getChild("tt_r3");
        ModelPart tt_l4 = jaw_tip.getChild("tt_l4");
        ModelPart tt_r4 = jaw_tip.getChild("tt_r4");
        ModelPart tf_l = jaw_tip.getChild("tf_l");
        ModelPart tf_r = jaw_tip.getChild("tf_r");
        ModelPart tu1_l0 = snout1.getChild("tu1_l0");
        ModelPart tu1_r0 = snout1.getChild("tu1_r0");
        ModelPart tu1_l1 = snout1.getChild("tu1_l1");
        ModelPart tu1_r1 = snout1.getChild("tu1_r1");
        ModelPart tu1_l2 = snout1.getChild("tu1_l2");
        ModelPart tu1_r2 = snout1.getChild("tu1_r2");
        ModelPart tu1_l3 = snout1.getChild("tu1_l3");
        ModelPart tu1_r3 = snout1.getChild("tu1_r3");
        ModelPart tu1_l4 = snout1.getChild("tu1_l4");
        ModelPart tu1_r4 = snout1.getChild("tu1_r4");
        ModelPart tu2_l0 = snout2.getChild("tu2_l0");
        ModelPart tu2_r0 = snout2.getChild("tu2_r0");
        ModelPart tu2_l1 = snout2.getChild("tu2_l1");
        ModelPart tu2_r1 = snout2.getChild("tu2_r1");
        ModelPart tu2_l2 = snout2.getChild("tu2_l2");
        ModelPart tu2_r2 = snout2.getChild("tu2_r2");
        ModelPart tu2_l3 = snout2.getChild("tu2_l3");
        ModelPart tu2_r3 = snout2.getChild("tu2_r3");
        ModelPart tu3_l0 = snout3.getChild("tu3_l0");
        ModelPart tu3_r0 = snout3.getChild("tu3_r0");
        ModelPart tu3_l1 = snout3.getChild("tu3_l1");
        ModelPart tu3_r1 = snout3.getChild("tu3_r1");
        ModelPart tu3_l2 = snout3.getChild("tu3_l2");
        ModelPart tu3_r2 = snout3.getChild("tu3_r2");
        ModelPart tu4_l0 = snout4.getChild("tu4_l0");
        ModelPart tu4_r0 = snout4.getChild("tu4_r0");
        ModelPart tu4_l1 = snout4.getChild("tu4_l1");
        ModelPart tu4_r1 = snout4.getChild("tu4_r1");
        ModelPart tuf_l = snout4.getChild("tuf_l");
        ModelPart tuf_r = snout4.getChild("tuf_r");
        ModelPart eyelid_l = head.getChild("eyelid_l");
        ModelPart eyelid_r = head.getChild("eyelid_r");
        ModelPart brow_l = head.getChild("brow_l");
        ModelPart brow_r = head.getChild("brow_r");
        ModelPart lure1 = melon.getChild("lure1");
        ModelPart lure2 = lure1.getChild("lure2");
        ModelPart lure3 = lure2.getChild("lure3");
        ModelPart lure_bulb = lure3.getChild("lure_bulb");
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
        this.hind_l = hind_l;
        this.hind_r = hind_r;
        this.eyelid_l = eyelid_l;
        this.eyelid_r = eyelid_r;
        this.tongue = tongue;
        this.tail4 = tail4;
        this.brow_l = brow_l;
        this.brow_r = brow_r;
        this.lure1 = lure1;
        this.lure2 = lure2;
        this.lure3 = lure3;
        for (ModelPart p : new ModelPart[]{body, mid, rear, tail1, tail2, tail3, fluke, head, jaw, jaw_tip, crest, crest_tip, horn_l, horn_r, dorsal1, dorsal2, dorsal3, dorsal4, ventral1, ventral2, ventral3, pec_l1, pec_l2, pec_l3, pec_r1, pec_r2, pec_r3, wing_l1, wing_l2, wing_r1, wing_r2, fl_l1, fl_l2, fl_l3, fl_r1, fl_r2, fl_r3, tailspike, tailkeel, hind_l, hind_r, eyelid_l, eyelid_r, tongue, tail4, brow_l, brow_r, lure1, lure2, lure3}) {
            this.rest.put(p, new float[]{p.xRot, p.yRot, p.zRot, p.y});
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition v_body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -11.0F, -13.0F, 24.0F, 22.0F, 26.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition v_mid = v_body.addOrReplaceChild("mid",
                CubeListBuilder.create().texOffs(100, 0).addBox(-13.0F, -12.0F, 0.0F, 26.0F, 24.0F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 13.0F));
        PartDefinition v_rear = v_mid.addOrReplaceChild("rear",
                CubeListBuilder.create().texOffs(0, 48).addBox(-9.0F, -9.0F, 0.0F, 18.0F, 18.0F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 18.0F));
        PartDefinition v_tail1 = v_rear.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(78, 84).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 18.0F));
        PartDefinition v_tail2 = v_tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(192, 84).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 16.0F));
        PartDefinition v_tail3 = v_tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(102, 113).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 6.0F, 14.0F),
                PartPose.offset(0.0F, 0.0F, 16.0F));
        PartDefinition v_tail4 = v_tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 155).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 10.0F),
                PartPose.offset(0.0F, 0.0F, 14.0F));
        PartDefinition v_head = v_body.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(134, 84).addBox(-8.0F, -7.5F, -13.16F, 16.0F, 15.0F, 13.0F),
                PartPose.offset(0.0F, 0.0F, -13.0F));
        PartDefinition v_melon = v_head.addOrReplaceChild("melon",
                CubeListBuilder.create().texOffs(112, 155).addBox(-5.0F, -10.51F, -11.9F, 10.0F, 4.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_snout1 = v_head.addOrReplaceChild("snout1",
                CubeListBuilder.create().texOffs(184, 113).addBox(-6.5F, -5.24F, -10.18F, 13.0F, 9.0F, 10.0F),
                PartPose.offset(0.0F, 1.48F, -13.32F));
        PartDefinition v_snout2 = v_snout1.addOrReplaceChild("snout2",
                CubeListBuilder.create().texOffs(180, 137).addBox(-5.0F, -3.87F, -8.94F, 10.0F, 7.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, -10.36F));
        PartDefinition v_snout3 = v_snout2.addOrReplaceChild("snout3",
                CubeListBuilder.create().texOffs(14, 170).addBox(-3.5F, -2.87F, -6.83F, 7.0F, 5.0F, 7.0F),
                PartPose.offset(0.0F, 0.0F, -8.879999999999999F));
        PartDefinition v_snout4 = v_snout3.addOrReplaceChild("snout4",
                CubeListBuilder.create().texOffs(16, 194).addBox(-2.0F, -2.3699999999999997F, -3.85F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, -6.66F));
        PartDefinition v_jaw = v_head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(0, 84).addBox(-6.0F, 3.07F, -26.82F, 12.0F, 2.0F, 27.0F),
                PartPose.offsetAndRotation(0.0F, 7.4F, -1.48F, 0.12F, 0.0F, 0.0F));
        PartDefinition v_jaw_tip = v_jaw.addOrReplaceChild("jaw_tip",
                CubeListBuilder.create().texOffs(84, 137).addBox(-3.5F, 1.33F, -13.16F, 7.0F, 4.0F, 13.0F),
                PartPose.offset(0.0F, 0.0F, -26.64F));
        PartDefinition v_wall_l = v_jaw.addOrReplaceChild("wall_l",
                CubeListBuilder.create().texOffs(72, 48).addBox(0.24F, -1.2600000000000002F, -26.82F, 1.0F, 4.0F, 27.0F),
                PartPose.offset(4.4399999999999995F, 0.0F, 0.0F));
        PartDefinition v_wall_r = v_jaw.addOrReplaceChild("wall_r",
                CubeListBuilder.create().texOffs(128, 48).addBox(-1.24F, -1.2600000000000002F, -26.82F, 1.0F, 4.0F, 27.0F),
                PartPose.offset(-4.4399999999999995F, 0.0F, 0.0F));
        PartDefinition v_wallt_l = v_jaw_tip.addOrReplaceChild("wallt_l",
                CubeListBuilder.create().texOffs(124, 137).addBox(0.24F, -1.2600000000000002F, -13.16F, 1.0F, 4.0F, 13.0F),
                PartPose.offset(2.2199999999999998F, 0.0F, 0.0F));
        PartDefinition v_wallt_r = v_jaw_tip.addOrReplaceChild("wallt_r",
                CubeListBuilder.create().texOffs(152, 137).addBox(-1.24F, -1.2600000000000002F, -13.16F, 1.0F, 4.0F, 13.0F),
                PartPose.offset(-2.2199999999999998F, 0.0F, 0.0F));
        PartDefinition v_lip = v_jaw_tip.addOrReplaceChild("lip",
                CubeListBuilder.create().texOffs(192, 194).addBox(-3.5F, -1.2600000000000002F, -13.08F, 7.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_palate0 = v_head.addOrReplaceChild("palate0",
                CubeListBuilder.create().texOffs(56, 155).addBox(-7.5F, 7.2700000000000005F, -13.16F, 15.0F, 1.0F, 13.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_palate1 = v_snout1.addOrReplaceChild("palate1",
                CubeListBuilder.create().texOffs(62, 170).addBox(-6.0F, 3.5700000000000003F, -10.18F, 12.0F, 1.0F, 10.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_palate2 = v_snout2.addOrReplaceChild("palate2",
                CubeListBuilder.create().texOffs(14, 183).addBox(-4.5F, 2.83F, -8.94F, 9.0F, 1.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_palate3 = v_snout3.addOrReplaceChild("palate3",
                CubeListBuilder.create().texOffs(32, 194).addBox(-3.0F, 2.09F, -6.83F, 6.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_palate4 = v_snout4.addOrReplaceChild("palate4",
                CubeListBuilder.create().texOffs(208, 194).addBox(-1.5F, 1.35F, -3.85F, 3.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_cheek_l = v_head.addOrReplaceChild("cheek_l",
                CubeListBuilder.create().texOffs(0, 137).addBox(6.029999999999999F, 7.49F, -13.16F, 2.0F, 5.0F, 13.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_cheek_r = v_head.addOrReplaceChild("cheek_r",
                CubeListBuilder.create().texOffs(30, 137).addBox(-8.030000000000001F, 7.49F, -13.16F, 2.0F, 5.0F, 13.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_throat = v_head.addOrReplaceChild("throat",
                CubeListBuilder.create().texOffs(50, 183).addBox(-7.5F, 7.23F, -2.98F, 15.0F, 7.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_eye_l_big = v_head.addOrReplaceChild("eye_l_big",
                CubeListBuilder.create().texOffs(16, 213).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(8.14F, -1.48F, -8.879999999999999F));
        PartDefinition v_eye_r_big = v_head.addOrReplaceChild("eye_r_big",
                CubeListBuilder.create().texOffs(24, 213).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-8.14F, -1.48F, -8.879999999999999F));
        PartDefinition v_eye_l_a = v_head.addOrReplaceChild("eye_l_a",
                CubeListBuilder.create().texOffs(56, 213).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(8.14F, -5.18F, -4.4399999999999995F));
        PartDefinition v_eye_r_a = v_head.addOrReplaceChild("eye_r_a",
                CubeListBuilder.create().texOffs(60, 213).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-8.14F, -5.18F, -4.4399999999999995F));
        PartDefinition v_eye_l_b = v_head.addOrReplaceChild("eye_l_b",
                CubeListBuilder.create().texOffs(64, 213).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(8.14F, 2.96F, -4.4399999999999995F));
        PartDefinition v_eye_r_b = v_head.addOrReplaceChild("eye_r_b",
                CubeListBuilder.create().texOffs(68, 213).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-8.14F, 2.96F, -4.4399999999999995F));
        PartDefinition v_eye_l_c = v_snout1.addOrReplaceChild("eye_l_c",
                CubeListBuilder.create().texOffs(72, 213).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(6.66F, -0.74F, -5.92F));
        PartDefinition v_eye_r_c = v_snout1.addOrReplaceChild("eye_r_c",
                CubeListBuilder.create().texOffs(76, 213).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-6.66F, -0.74F, -5.92F));
        PartDefinition v_eye_l_d = v_head.addOrReplaceChild("eye_l_d",
                CubeListBuilder.create().texOffs(80, 213).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(8.14F, -5.18F, -11.84F));
        PartDefinition v_eye_r_d = v_head.addOrReplaceChild("eye_r_d",
                CubeListBuilder.create().texOffs(84, 213).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-8.14F, -5.18F, -11.84F));
        PartDefinition v_eye_l_e = v_head.addOrReplaceChild("eye_l_e",
                CubeListBuilder.create().texOffs(88, 213).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(8.14F, 3.7F, -11.84F));
        PartDefinition v_eye_r_e = v_head.addOrReplaceChild("eye_r_e",
                CubeListBuilder.create().texOffs(92, 213).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-8.14F, 3.7F, -11.84F));
        PartDefinition v_crest = v_head.addOrReplaceChild("crest",
                CubeListBuilder.create().texOffs(106, 170).addBox(-1.5F, -2.11F, -8.94F, 3.0F, 2.0F, 9.0F),
                PartPose.offset(0.0F, -7.4F, -4.4399999999999995F));
        PartDefinition v_crest_tip = v_crest.addOrReplaceChild("crest_tip",
                CubeListBuilder.create().texOffs(222, 194).addBox(-0.5F, -1.24F, -4.22F, 1.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, -0.74F, -8.879999999999999F));
        PartDefinition v_horn_l = v_head.addOrReplaceChild("horn_l",
                CubeListBuilder.create().texOffs(214, 183).addBox(0.10999999999999988F, -2.11F, -1.2799999999999998F, 2.0F, 2.0F, 7.0F),
                PartPose.offset(7.4F, -8.14F, -10.36F));
        PartDefinition v_horn_r = v_head.addOrReplaceChild("horn_r",
                CubeListBuilder.create().texOffs(232, 183).addBox(-2.11F, -2.11F, -1.2799999999999998F, 2.0F, 2.0F, 7.0F),
                PartPose.offset(-7.4F, -8.14F, -10.36F));
        PartDefinition v_gill_l = v_head.addOrReplaceChild("gill_l",
                CubeListBuilder.create().texOffs(28, 155).addBox(-0.13F, -4.5F, -0.040000000000000036F, 1.0F, 9.0F, 6.0F),
                PartPose.offset(8.14F, -0.74F, -5.92F));
        PartDefinition v_gill_r = v_head.addOrReplaceChild("gill_r",
                CubeListBuilder.create().texOffs(42, 155).addBox(-0.87F, -4.5F, -0.040000000000000036F, 1.0F, 9.0F, 6.0F),
                PartPose.offset(-8.14F, -0.74F, -5.92F));
        PartDefinition v_dorsal1 = v_mid.addOrReplaceChild("dorsal1",
                CubeListBuilder.create().texOffs(0, 113).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 12.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 4.0F, -0.25F, 0.0F, 0.0F));
        PartDefinition v_dorsal2 = v_dorsal1.addOrReplaceChild("dorsal2",
                CubeListBuilder.create().texOffs(230, 113).addBox(-1.0F, -10.0F, 0.0F, 2.0F, 10.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 3.0F, -0.3F, 0.0F, 0.0F));
        PartDefinition v_dorsal3 = v_dorsal2.addOrReplaceChild("dorsal3",
                CubeListBuilder.create().texOffs(150, 155).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 3.0F, -0.35F, 0.0F, 0.0F));
        PartDefinition v_dorsal4 = v_dorsal3.addOrReplaceChild("dorsal4",
                CubeListBuilder.create().texOffs(82, 194).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, 2.0F, -0.4F, 0.0F, 0.0F));
        PartDefinition v_dorsal_b = v_rear.addOrReplaceChild("dorsal_b",
                CubeListBuilder.create().texOffs(130, 170).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, -0.4F, 0.0F, 0.0F));
        PartDefinition v_tailspike = v_tail1.addOrReplaceChild("tailspike",
                CubeListBuilder.create().texOffs(146, 170).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 4.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, 2.0F, -0.4F, 0.0F, 0.0F));
        PartDefinition v_tailkeel = v_tail2.addOrReplaceChild("tailkeel",
                CubeListBuilder.create().texOffs(86, 183).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, 0.5F, 0.0F, 0.0F));
        PartDefinition v_ventral1 = v_rear.addOrReplaceChild("ventral1",
                CubeListBuilder.create().texOffs(42, 170).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 3.0F, 0.3F, 0.0F, 0.0F));
        PartDefinition v_ventral2 = v_ventral1.addOrReplaceChild("ventral2",
                CubeListBuilder.create().texOffs(0, 194).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 2.0F, 0.3F, 0.0F, 0.0F));
        PartDefinition v_ventral3 = v_ventral2.addOrReplaceChild("ventral3",
                CubeListBuilder.create().texOffs(160, 194).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 2.0F, 0.3F, 0.0F, 0.0F));
        PartDefinition v_pec_l1 = v_body.addOrReplaceChild("pec_l1",
                CubeListBuilder.create().texOffs(166, 155).addBox(0.0F, -1.5F, -4.0F, 12.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(12.0F, 10.0F, -4.0F, 0.0F, -0.45F, 0.8F));
        PartDefinition v_pec_r1 = v_body.addOrReplaceChild("pec_r1",
                CubeListBuilder.create().texOffs(210, 155).addBox(-12.0F, -1.5F, -4.0F, 12.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(-12.0F, 10.0F, -4.0F, 0.0F, 0.45F, -0.8F));
        PartDefinition v_pec_l2 = v_pec_l1.addOrReplaceChild("pec_l2",
                CubeListBuilder.create().texOffs(164, 170).addBox(0.0F, -1.0F, -3.0F, 12.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(12.0F, 0.0F, 0.0F, 0.0F, -0.3F, 0.15F));
        PartDefinition v_pec_r2 = v_pec_r1.addOrReplaceChild("pec_r2",
                CubeListBuilder.create().texOffs(206, 170).addBox(-12.0F, -1.0F, -3.0F, 12.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(-12.0F, 0.0F, 0.0F, 0.0F, 0.3F, -0.15F));
        PartDefinition v_pec_l3 = v_pec_l2.addOrReplaceChild("pec_l3",
                CubeListBuilder.create().texOffs(92, 194).addBox(0.0F, -0.5F, -2.0F, 10.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(12.0F, 0.0F, 0.0F, 0.0F, -0.3F, 0.1F));
        PartDefinition v_pec_r3 = v_pec_r2.addOrReplaceChild("pec_r3",
                CubeListBuilder.create().texOffs(124, 194).addBox(-10.0F, -0.5F, -2.0F, 10.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-12.0F, 0.0F, 0.0F, 0.0F, 0.3F, -0.1F));
        PartDefinition v_hind_l = v_rear.addOrReplaceChild("hind_l",
                CubeListBuilder.create().texOffs(102, 183).addBox(0.0F, -1.0F, -2.0F, 9.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(9.0F, 6.0F, 4.0F, 0.0F, -0.4F, 0.6F));
        PartDefinition v_hind_r = v_rear.addOrReplaceChild("hind_r",
                CubeListBuilder.create().texOffs(136, 183).addBox(-9.0F, -1.0F, -2.0F, 9.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-9.0F, 6.0F, 4.0F, 0.0F, 0.4F, -0.6F));
        PartDefinition v_wing_l1 = v_head.addOrReplaceChild("wing_l1",
                CubeListBuilder.create().texOffs(232, 194).addBox(-0.1499999999999999F, -0.5F, -0.5200000000000002F, 4.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(8.14F, 5.92F, -7.4F, 0.0F, -0.6F, 0.5F));
        PartDefinition v_wing_r1 = v_head.addOrReplaceChild("wing_r1",
                CubeListBuilder.create().texOffs(0, 203).addBox(-3.85F, -0.5F, -0.5200000000000002F, 4.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-8.14F, 5.92F, -7.4F, 0.0F, 0.6F, -0.5F));
        PartDefinition v_wing_l2 = v_wing_l1.addOrReplaceChild("wing_l2",
                CubeListBuilder.create().texOffs(32, 213).addBox(-0.020000000000000018F, -0.13F, -0.76F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(3.7F, 0.0F, 0.0F, 0.0F, -0.5F, 0.2F));
        PartDefinition v_wing_r2 = v_wing_r1.addOrReplaceChild("wing_r2",
                CubeListBuilder.create().texOffs(44, 213).addBox(-2.98F, -0.13F, -0.76F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-3.7F, 0.0F, 0.0F, 0.0F, 0.5F, -0.2F));
        PartDefinition v_fluke = v_tail4.addOrReplaceChild("fluke",
                CubeListBuilder.create().texOffs(60, 137).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 8.0F, 10.0F),
                PartPose.offset(0.0F, 0.0F, 9.0F));
        PartDefinition v_fl_l1 = v_fluke.addOrReplaceChild("fl_l1",
                CubeListBuilder.create().texOffs(28, 113).addBox(-1.0F, -14.0F, -2.0F, 2.0F, 14.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, 2.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition v_fl_l2 = v_fl_l1.addOrReplaceChild("fl_l2",
                CubeListBuilder.create().texOffs(140, 113).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, -0.45F, 0.0F, 0.0F));
        PartDefinition v_fl_l3 = v_fl_l2.addOrReplaceChild("fl_l3",
                CubeListBuilder.create().texOffs(0, 170).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, -0.4F, 0.0F, 0.0F));
        PartDefinition v_fl_r1 = v_fluke.addOrReplaceChild("fl_r1",
                CubeListBuilder.create().texOffs(160, 113).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 10.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 2.0F, 0.5F, 0.0F, 0.0F));
        PartDefinition v_fl_r2 = v_fl_r1.addOrReplaceChild("fl_r2",
                CubeListBuilder.create().texOffs(218, 137).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.45F, 0.0F, 0.0F));
        PartDefinition v_fl_r3 = v_fl_r2.addOrReplaceChild("fl_r3",
                CubeListBuilder.create().texOffs(0, 183).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.4F, 0.0F, 0.0F));
        PartDefinition v_tongue = v_jaw.addOrReplaceChild("tongue",
                CubeListBuilder.create().texOffs(52, 113).addBox(-2.0F, 2.09F, -23.82F, 4.0F, 1.0F, 21.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition v_tl_l0 = v_jaw.addOrReplaceChild("tl_l0",
                CubeListBuilder.create().texOffs(16, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -2.96F));
        PartDefinition v_tl_r0 = v_jaw.addOrReplaceChild("tl_r0",
                CubeListBuilder.create().texOffs(24, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -2.96F));
        PartDefinition v_tl_l1 = v_jaw.addOrReplaceChild("tl_l1",
                CubeListBuilder.create().texOffs(32, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -5.18F));
        PartDefinition v_tl_r1 = v_jaw.addOrReplaceChild("tl_r1",
                CubeListBuilder.create().texOffs(40, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -5.18F));
        PartDefinition v_tl_l2 = v_jaw.addOrReplaceChild("tl_l2",
                CubeListBuilder.create().texOffs(48, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -7.4F));
        PartDefinition v_tl_r2 = v_jaw.addOrReplaceChild("tl_r2",
                CubeListBuilder.create().texOffs(56, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -7.4F));
        PartDefinition v_tl_l3 = v_jaw.addOrReplaceChild("tl_l3",
                CubeListBuilder.create().texOffs(64, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -9.62F));
        PartDefinition v_tl_r3 = v_jaw.addOrReplaceChild("tl_r3",
                CubeListBuilder.create().texOffs(72, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -9.62F));
        PartDefinition v_tl_l4 = v_jaw.addOrReplaceChild("tl_l4",
                CubeListBuilder.create().texOffs(80, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -11.84F));
        PartDefinition v_tl_r4 = v_jaw.addOrReplaceChild("tl_r4",
                CubeListBuilder.create().texOffs(88, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -11.84F));
        PartDefinition v_tl_l5 = v_jaw.addOrReplaceChild("tl_l5",
                CubeListBuilder.create().texOffs(96, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -14.06F));
        PartDefinition v_tl_r5 = v_jaw.addOrReplaceChild("tl_r5",
                CubeListBuilder.create().texOffs(104, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -14.06F));
        PartDefinition v_tl_l6 = v_jaw.addOrReplaceChild("tl_l6",
                CubeListBuilder.create().texOffs(112, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -16.28F));
        PartDefinition v_tl_r6 = v_jaw.addOrReplaceChild("tl_r6",
                CubeListBuilder.create().texOffs(120, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -16.28F));
        PartDefinition v_tl_l7 = v_jaw.addOrReplaceChild("tl_l7",
                CubeListBuilder.create().texOffs(128, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -18.5F));
        PartDefinition v_tl_r7 = v_jaw.addOrReplaceChild("tl_r7",
                CubeListBuilder.create().texOffs(136, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -18.5F));
        PartDefinition v_tl_l8 = v_jaw.addOrReplaceChild("tl_l8",
                CubeListBuilder.create().texOffs(144, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -20.72F));
        PartDefinition v_tl_r8 = v_jaw.addOrReplaceChild("tl_r8",
                CubeListBuilder.create().texOffs(152, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -20.72F));
        PartDefinition v_tl_l9 = v_jaw.addOrReplaceChild("tl_l9",
                CubeListBuilder.create().texOffs(160, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -22.94F));
        PartDefinition v_tl_r9 = v_jaw.addOrReplaceChild("tl_r9",
                CubeListBuilder.create().texOffs(168, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -22.94F));
        PartDefinition v_tl_l10 = v_jaw.addOrReplaceChild("tl_l10",
                CubeListBuilder.create().texOffs(176, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.18F, -1.48F, -25.16F));
        PartDefinition v_tl_r10 = v_jaw.addOrReplaceChild("tl_r10",
                CubeListBuilder.create().texOffs(184, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.18F, -1.48F, -25.16F));
        PartDefinition v_tt_l0 = v_jaw_tip.addOrReplaceChild("tt_l0",
                CubeListBuilder.create().texOffs(192, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, -1.48F, -1.48F));
        PartDefinition v_tt_r0 = v_jaw_tip.addOrReplaceChild("tt_r0",
                CubeListBuilder.create().texOffs(200, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, -1.48F, -1.48F));
        PartDefinition v_tt_l1 = v_jaw_tip.addOrReplaceChild("tt_l1",
                CubeListBuilder.create().texOffs(208, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, -1.48F, -3.7F));
        PartDefinition v_tt_r1 = v_jaw_tip.addOrReplaceChild("tt_r1",
                CubeListBuilder.create().texOffs(216, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, -1.48F, -3.7F));
        PartDefinition v_tt_l2 = v_jaw_tip.addOrReplaceChild("tt_l2",
                CubeListBuilder.create().texOffs(224, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, -1.48F, -5.92F));
        PartDefinition v_tt_r2 = v_jaw_tip.addOrReplaceChild("tt_r2",
                CubeListBuilder.create().texOffs(232, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, -1.48F, -5.92F));
        PartDefinition v_tt_l3 = v_jaw_tip.addOrReplaceChild("tt_l3",
                CubeListBuilder.create().texOffs(240, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, -1.48F, -8.14F));
        PartDefinition v_tt_r3 = v_jaw_tip.addOrReplaceChild("tt_r3",
                CubeListBuilder.create().texOffs(248, 203).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, -1.48F, -8.14F));
        PartDefinition v_tt_l4 = v_jaw_tip.addOrReplaceChild("tt_l4",
                CubeListBuilder.create().texOffs(0, 208).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, -1.48F, -10.36F));
        PartDefinition v_tt_r4 = v_jaw_tip.addOrReplaceChild("tt_r4",
                CubeListBuilder.create().texOffs(8, 208).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, -1.48F, -10.36F));
        PartDefinition v_tf_l = v_jaw_tip.addOrReplaceChild("tf_l",
                CubeListBuilder.create().texOffs(16, 208).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(1.1099999999999999F, -1.48F, -12.58F));
        PartDefinition v_tf_r = v_jaw_tip.addOrReplaceChild("tf_r",
                CubeListBuilder.create().texOffs(24, 208).addBox(-0.74F, -2.86F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-1.1099999999999999F, -1.48F, -12.58F));
        PartDefinition v_tu1_l0 = v_snout1.addOrReplaceChild("tu1_l0",
                CubeListBuilder.create().texOffs(32, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.92F, 4.4399999999999995F, -0.74F));
        PartDefinition v_tu1_r0 = v_snout1.addOrReplaceChild("tu1_r0",
                CubeListBuilder.create().texOffs(40, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.92F, 4.4399999999999995F, -0.74F));
        PartDefinition v_tu1_l1 = v_snout1.addOrReplaceChild("tu1_l1",
                CubeListBuilder.create().texOffs(48, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.92F, 4.4399999999999995F, -2.96F));
        PartDefinition v_tu1_r1 = v_snout1.addOrReplaceChild("tu1_r1",
                CubeListBuilder.create().texOffs(56, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.92F, 4.4399999999999995F, -2.96F));
        PartDefinition v_tu1_l2 = v_snout1.addOrReplaceChild("tu1_l2",
                CubeListBuilder.create().texOffs(64, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.92F, 4.4399999999999995F, -5.18F));
        PartDefinition v_tu1_r2 = v_snout1.addOrReplaceChild("tu1_r2",
                CubeListBuilder.create().texOffs(72, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.92F, 4.4399999999999995F, -5.18F));
        PartDefinition v_tu1_l3 = v_snout1.addOrReplaceChild("tu1_l3",
                CubeListBuilder.create().texOffs(80, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.92F, 4.4399999999999995F, -7.4F));
        PartDefinition v_tu1_r3 = v_snout1.addOrReplaceChild("tu1_r3",
                CubeListBuilder.create().texOffs(88, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.92F, 4.4399999999999995F, -7.4F));
        PartDefinition v_tu1_l4 = v_snout1.addOrReplaceChild("tu1_l4",
                CubeListBuilder.create().texOffs(96, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(5.92F, 4.4399999999999995F, -9.62F));
        PartDefinition v_tu1_r4 = v_snout1.addOrReplaceChild("tu1_r4",
                CubeListBuilder.create().texOffs(104, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-5.92F, 4.4399999999999995F, -9.62F));
        PartDefinition v_tu2_l0 = v_snout2.addOrReplaceChild("tu2_l0",
                CubeListBuilder.create().texOffs(112, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(4.4399999999999995F, 3.7F, -0.74F));
        PartDefinition v_tu2_r0 = v_snout2.addOrReplaceChild("tu2_r0",
                CubeListBuilder.create().texOffs(120, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-4.4399999999999995F, 3.7F, -0.74F));
        PartDefinition v_tu2_l1 = v_snout2.addOrReplaceChild("tu2_l1",
                CubeListBuilder.create().texOffs(128, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(4.4399999999999995F, 3.7F, -2.96F));
        PartDefinition v_tu2_r1 = v_snout2.addOrReplaceChild("tu2_r1",
                CubeListBuilder.create().texOffs(136, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-4.4399999999999995F, 3.7F, -2.96F));
        PartDefinition v_tu2_l2 = v_snout2.addOrReplaceChild("tu2_l2",
                CubeListBuilder.create().texOffs(144, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(4.4399999999999995F, 3.7F, -5.18F));
        PartDefinition v_tu2_r2 = v_snout2.addOrReplaceChild("tu2_r2",
                CubeListBuilder.create().texOffs(152, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-4.4399999999999995F, 3.7F, -5.18F));
        PartDefinition v_tu2_l3 = v_snout2.addOrReplaceChild("tu2_l3",
                CubeListBuilder.create().texOffs(160, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(4.4399999999999995F, 3.7F, -7.4F));
        PartDefinition v_tu2_r3 = v_snout2.addOrReplaceChild("tu2_r3",
                CubeListBuilder.create().texOffs(168, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-4.4399999999999995F, 3.7F, -7.4F));
        PartDefinition v_tu3_l0 = v_snout3.addOrReplaceChild("tu3_l0",
                CubeListBuilder.create().texOffs(176, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, 2.96F, -0.74F));
        PartDefinition v_tu3_r0 = v_snout3.addOrReplaceChild("tu3_r0",
                CubeListBuilder.create().texOffs(184, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, 2.96F, -0.74F));
        PartDefinition v_tu3_l1 = v_snout3.addOrReplaceChild("tu3_l1",
                CubeListBuilder.create().texOffs(192, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, 2.96F, -2.96F));
        PartDefinition v_tu3_r1 = v_snout3.addOrReplaceChild("tu3_r1",
                CubeListBuilder.create().texOffs(200, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, 2.96F, -2.96F));
        PartDefinition v_tu3_l2 = v_snout3.addOrReplaceChild("tu3_l2",
                CubeListBuilder.create().texOffs(208, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.96F, 2.96F, -5.18F));
        PartDefinition v_tu3_r2 = v_snout3.addOrReplaceChild("tu3_r2",
                CubeListBuilder.create().texOffs(216, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.96F, 2.96F, -5.18F));
        PartDefinition v_tu4_l0 = v_snout4.addOrReplaceChild("tu4_l0",
                CubeListBuilder.create().texOffs(224, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(1.85F, 2.2199999999999998F, -0.74F));
        PartDefinition v_tu4_r0 = v_snout4.addOrReplaceChild("tu4_r0",
                CubeListBuilder.create().texOffs(232, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-1.85F, 2.2199999999999998F, -0.74F));
        PartDefinition v_tu4_l1 = v_snout4.addOrReplaceChild("tu4_l1",
                CubeListBuilder.create().texOffs(240, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(1.85F, 2.2199999999999998F, -2.96F));
        PartDefinition v_tu4_r1 = v_snout4.addOrReplaceChild("tu4_r1",
                CubeListBuilder.create().texOffs(248, 208).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-1.85F, 2.2199999999999998F, -2.96F));
        PartDefinition v_tuf_l = v_snout4.addOrReplaceChild("tuf_l",
                CubeListBuilder.create().texOffs(0, 213).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(0.74F, 2.2199999999999998F, -3.33F));
        PartDefinition v_tuf_r = v_snout4.addOrReplaceChild("tuf_r",
                CubeListBuilder.create().texOffs(8, 213).addBox(-0.74F, 0.10000000000000009F, -0.74F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-0.74F, 2.2199999999999998F, -3.33F));
        PartDefinition v_eyelid_l = v_head.addOrReplaceChild("eyelid_l",
                CubeListBuilder.create().texOffs(172, 194).addBox(0.0F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F),
                PartPose.offset(8.14F, -3.7F, -8.879999999999999F));
        PartDefinition v_eyelid_r = v_head.addOrReplaceChild("eyelid_r",
                CubeListBuilder.create().texOffs(182, 194).addBox(-2.0F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F),
                PartPose.offset(-8.14F, -3.7F, -8.879999999999999F));
        PartDefinition v_brow_l = v_head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(170, 183).addBox(0.10999999999999988F, -1.24F, -4.5F, 2.0F, 1.0F, 9.0F),
                PartPose.offset(7.4F, -8.879999999999999F, -10.36F));
        PartDefinition v_brow_r = v_head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(192, 183).addBox(-2.11F, -1.24F, -4.5F, 2.0F, 1.0F, 9.0F),
                PartPose.offset(-7.4F, -8.879999999999999F, -10.36F));
        PartDefinition v_lure1 = v_melon.addOrReplaceChild("lure1",
                CubeListBuilder.create().texOffs(58, 194).addBox(-0.5F, -7.2F, -0.5F, 1.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -10.36F, -7.4F, 0.6F, 0.0F, 0.0F));
        PartDefinition v_lure2 = v_lure1.addOrReplaceChild("lure2",
                CubeListBuilder.create().texOffs(62, 194).addBox(-0.5F, -6.83F, -0.5F, 1.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.4F, 0.0F, 0.5F, 0.0F, 0.0F));
        PartDefinition v_lure3 = v_lure2.addOrReplaceChild("lure3",
                CubeListBuilder.create().texOffs(156, 194).addBox(-0.5F, -5.96F, -0.5F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -6.66F, 0.0F, 0.9F, 0.0F, 0.0F));
        PartDefinition v_lure_bulb = v_lure3.addOrReplaceChild("lure_bulb",
                CubeListBuilder.create().texOffs(66, 194).addBox(-2.0F, -4.22F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, -5.92F, 0.0F));

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
        // Varado en tierra: se sacude como un pez fuera del agua.
        boolean beached = !entity.isInWater() && entity.onGround();
        float flop = beached ? 1.0F : 0.0F;
        float phase = ageInTicks * 0.035F * (beached ? 3.5F : 1.0F) + limbSwing * 0.25F;
        float amp = 0.8F + move * 1.0F + (angry ? 0.5F : 0.0F) + flop * 1.6F;

        // Mordida: la animación de ataque de la entidad abre y cierra la mandíbula.
        float bite = this.attackTime > 0.0F ? Mth.sin(this.attackTime * (float) Math.PI) : 0.0F;

        // Estados del ataque a distancia (solo el adulto; en la cría valen 0).
        float charge = atk == 2 ? Mth.clamp(entity.getPhaseTimer() / 50.0F, 0.0F, 1.0F) : 0.0F;
        float fire = atk == 3 ? 1.0F : 0.0F;
        float recoil = Mth.clamp(entity.getRecoil() / 6.0F, 0.0F, 1.0F);
        float mouth = atk == 1 ? 0.45F + recoil * 0.4F : atk == 2 ? 0.30F + charge * 0.55F : atk == 3 ? 1.0F : 0.0F;
        float rage = (angry || atk > 0) ? 1.0F : 0.0F;

        // Reacciones: golpe recibido, muerte, giro y movimiento vertical.
        float hit = entity.hurtTime > 0 ? Mth.sin(entity.hurtTime * 0.9F) : 0.0F;
        float death = Mth.clamp(entity.deathTime / 20.0F, 0.0F, 1.0F);
        float turn = Mth.clamp(Mth.wrapDegrees(entity.getYRot() - entity.yRotO), -12.0F, 12.0F);
        float vy = Mth.clamp((float) entity.getDeltaMovement().y, -0.4F, 0.4F);
        float shake = Mth.sin(ageInTicks * 2.7F) * fire;   // vibración mientras dispara el rayo

        // ---- Cuerpo: arqueo al cargar, retroceso al disparar, balanceo en los giros, sacudida al recibir daño ----
        this.pose(this.body,
                headPitch * deg * 0.7F + Mth.sin(phase) * 0.02F * amp - charge * 0.18F - recoil * 0.10F - fire * 0.06F,
                Mth.sin(phase) * 0.025F * amp,
                -turn * 0.012F + hit * 0.05F);
        this.body.y = this.rest.get(this.body)[3] + Mth.sin(ageInTicks * 0.04F) * 0.6F + hit * 0.8F + shake * 0.3F;

        // ---- Onda LATERAL (tiburón/mosasaurio): el cuerpo serpentea de lado a lado y la cola se arrastra al subir/bajar y en los giros ----
        this.pose(this.mid,   Mth.sin(phase - 0.5F) * 0.015F * amp - vy * 0.2F,
                Mth.sin(phase - 0.4F) * 0.05F * amp - turn * 0.004F, 0.0F);
        this.pose(this.rear,  Mth.sin(phase - 1.0F) * 0.02F * amp - vy * 0.4F,
                Mth.sin(phase - 0.9F) * 0.09F * amp - turn * 0.008F, 0.0F);
        this.pose(this.tail1, Mth.sin(phase - 1.5F) * 0.03F * amp - vy * 0.6F + death * 0.10F,
                Mth.sin(phase - 1.4F) * 0.14F * amp - turn * 0.012F, 0.0F);
        this.pose(this.tail2, Mth.sin(phase - 2.0F) * 0.03F * amp - vy * 0.8F + death * 0.20F,
                Mth.sin(phase - 1.9F) * 0.18F * amp - turn * 0.016F, 0.0F);
        this.pose(this.tail3, Mth.sin(phase - 2.5F) * 0.03F * amp - vy * 1.0F + death * 0.30F,
                Mth.sin(phase - 2.4F) * 0.22F * amp - turn * 0.020F, 0.0F);
        this.pose(this.fluke, -vy * 1.0F, Mth.sin(phase - 2.9F) * 0.25F * amp, 0.0F);

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
                Mth.sin(phase - 0.2F) * 0.03F * amp + headPitch * deg * 0.3F + bite * 0.12F
                        - recoil * 0.25F - charge * 0.20F - fire * 0.12F + hit * 0.10F,
                netHeadYaw * deg * 0.35F + shake * 0.03F, 0.0F);
        this.pose(this.jaw,
                0.06F + Mth.sin(ageInTicks * 0.07F) * 0.04F + (angry ? 0.15F : 0.0F) + bite * 0.85F
                        + mouth * 0.85F + death * 0.50F
                        + Mth.sin(ageInTicks * 1.7F) * 0.03F * (charge + fire) + shake * 0.04F,
                0.0F, 0.0F);
        this.pose(this.jaw_tip, bite * 0.2F + mouth * 0.15F, 0.0F, 0.0F);

        // Cresta: se mece; al enfadarse o cargar se levanta
        float crestUp = rage * 0.3F + charge * 0.2F;
        this.pose(this.crest, Mth.sin(phase - 0.3F) * 0.04F * amp + crestUp, 0.0F, 0.0F);
        this.pose(this.crest_tip, Mth.sin(phase - 0.8F) * 0.06F * amp + crestUp * 0.6F, 0.0F, 0.0F);
        this.pose(this.horn_l, crestUp * 0.5F, 0.0F, rage * 0.25F);
        this.pose(this.horn_r, crestUp * 0.5F, 0.0F, -rage * 0.25F);

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

        // ---- Aletas pectorales: remada lenta; al enfadarse/cargar se abren; al morir caen ----
        float flare = rage * 0.3F + charge * 0.25F + fire * 0.15F;
        float lift = rage * -0.25F;
        float p1 = Mth.sin(phase + 0.4F) * 0.14F * amp + lift + death * 0.90F + shake * 0.05F;
        float p2 = Mth.sin(phase + 0.9F) * 0.20F * amp + death * 0.60F;
        float p3 = Mth.sin(phase + 1.4F) * 0.26F * amp + death * 0.40F;
        this.pose(this.pec_l1, 0.0F, flare, p1);
        this.pose(this.pec_l2, 0.0F, flare * 0.5F, p2);
        this.pose(this.pec_l3, 0.0F, flare * 0.3F, p3);
        this.pose(this.pec_r1, 0.0F, -flare, -p1);
        this.pose(this.pec_r2, 0.0F, -flare * 0.5F, -p2);
        this.pose(this.pec_r3, 0.0F, -flare * 0.3F, -p3);

        // ---- Aletas laterales superiores ----
        float w1 = Mth.sin(phase + 1.0F) * 0.10F * amp + rage * 0.2F + death * 0.50F;
        float w2 = Mth.sin(phase + 1.6F) * 0.16F * amp;
        this.pose(this.wing_l1, 0.0F, flare * 0.6F, w1);
        this.pose(this.wing_l2, 0.0F, 0.0F, w2);
        this.pose(this.wing_r1, 0.0F, -flare * 0.6F, -w1);
        this.pose(this.wing_r2, 0.0F, 0.0F, -w2);
        // Paletas traseras
        float hp = Mth.sin(phase + 1.2F) * 0.18F * amp + death * 0.5F;
        this.pose(this.hind_l, 0.0F, flare * 0.4F, hp);
        this.pose(this.hind_r, 0.0F, -flare * 0.4F, -hp);

        // ---- Extras (adolescente y adulto) ----
        // Parpadeo (cada ~4,5 s, desfasado por entidad); los ojos se cierran al morir
        float bt = (ageInTicks + entity.getId() * 37.0F) % 90.0F;
        float blink = bt < 6.0F ? Mth.sin(bt / 6.0F * (float) Math.PI) : 0.0F;
        blink = Math.max(blink * (atk > 0 ? 0.3F : 1.0F), death);
        this.eyelid_l.y = this.rest.get(this.eyelid_l)[3] + blink * 6.0F;
        this.eyelid_r.y = this.rest.get(this.eyelid_r)[3] + blink * 6.0F;
        this.pose(this.tongue, Mth.sin(ageInTicks * 0.12F) * 0.03F + mouth * 0.10F + Mth.sin(ageInTicks * 0.9F) * 0.02F * (charge + fire), 0.0F, 0.0F);
        this.pose(this.tail4, -vy * 1.0F + death * 0.3F, Mth.sin(phase - 2.7F) * 0.25F * amp - turn * 0.024F, 0.0F);

        this.pose(this.brow_l, rage * 0.18F, 0.0F, 0.0F);
        this.pose(this.brow_r, rage * 0.18F, 0.0F, 0.0F);

        // Anzuelo luminoso: péndulo que se agita al atacar y más aún al cargar
        float lureAmp = 1.0F + (atk > 0 ? 1.0F : 0.0F) + charge;
        this.pose(this.lure1, Mth.sin(phase * 1.3F - 0.3F) * 0.06F * lureAmp, Mth.sin(phase * 0.7F) * 0.05F * lureAmp, 0.0F);
        this.pose(this.lure2, Mth.sin(phase * 1.3F - 0.9F) * 0.09F * lureAmp, Mth.sin(phase * 0.7F - 0.5F) * 0.07F * lureAmp, 0.0F);
        this.pose(this.lure3, Mth.sin(phase * 1.3F - 1.5F) * 0.13F * lureAmp, Mth.sin(phase * 0.7F - 1.0F) * 0.10F * lureAmp, 0.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
