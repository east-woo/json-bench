package com.eastwoo.study.dto;

import com.dslplatform.json.CompiledJson;

import java.util.List;

public class FrameData {

    public Frm frm;

    public static class Frm {
        public int uid;
        public String tm;
        public int objN;
        public List<Obj> objs;
    }

    public static class Obj {
        public long id;
        public String type;
        public double score;
        public String stat;
        public String vclass; // optional
        public String vattr;  // optional
        public String pattr;  // optional
        public List<Double> box;
        public double speed;
        public List<Double> chtg;
    }
}