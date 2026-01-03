package com.eastwoo.study;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eastwoo.study.dto.FrameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LargeJsonGenerator {

    public static String generateLargeJson(int objCount) throws Exception {
        FrameData.Frm frm = new FrameData.Frm();
        frm.uid = 3004;
        frm.tm = "20260102T180822.181";
        frm.objN = objCount;

        List<FrameData.Obj> objs = new ArrayList<>(objCount);
        Random rand = new Random();

        for (int i = 0; i < objCount; i++) {
            FrameData.Obj obj = new FrameData.Obj();
            obj.id = 500000 + i;
            obj.type = rand.nextBoolean() ? "1" : "2";
            obj.score = rand.nextDouble();
            obj.stat = "2";
            obj.vclass = "0";
            obj.vattr = "2";
            obj.pattr = rand.nextBoolean() ? "0" : "20081";
            obj.box = Arrays.asList(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            obj.speed = -1.0;

            List<Double> chtg = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                chtg.add(rand.nextDouble());
            }
            obj.chtg = chtg;

            objs.add(obj);
        }

        frm.objs = objs;

        FrameData frameData = new FrameData();
        frameData.frm = frm;

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(frameData);
    }
}