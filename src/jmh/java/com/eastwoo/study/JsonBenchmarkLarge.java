package com.eastwoo.study;

import com.dslplatform.json.DslJson;
import com.eastwoo.study.dto.FrameData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import com.dslplatform.json.runtime.Settings;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class JsonBenchmarkLarge {

    private byte[] jsonBytes;
    private ObjectMapper objectMapperSingleton;
    private DslJson<Object> dslJson;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        String jsonString = LargeJsonGenerator.generateLargeJson(10000);
        jsonBytes = jsonString.getBytes();

        objectMapperSingleton = new ObjectMapper();

        dslJson = new DslJson<>(Settings.withRuntime().includeServiceLoader());
    }

    // ========== 타입 변환 ==========

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void a_dslJson(Blackhole bh) throws IOException {
        FrameData data = dslJson.deserialize(
                FrameData.class,
                jsonBytes,
                jsonBytes.length
        );
        bh.consume(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void b_jacksonSingleton(Blackhole bh) throws IOException {
        FrameData data = objectMapperSingleton.readValue(jsonBytes, FrameData.class);
        bh.consume(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void c_jacksonNewInstance(Blackhole bh) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FrameData data = mapper.readValue(jsonBytes, FrameData.class);
        bh.consume(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void d_jsoniter(Blackhole bh) throws IOException {
        FrameData data = JsonIterator.deserialize(jsonBytes, FrameData.class);
        bh.consume(data);
    }

    // ========== 동적 파싱  ==========

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void e_jsoniterAny(Blackhole bh) throws IOException {
        Any data = JsonIterator.deserialize(jsonBytes);
        bh.consume(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void f_jacksonJsonNode(Blackhole bh) throws IOException {
        JsonNode node = objectMapperSingleton.readTree(jsonBytes);
        bh.consume(node);
    }
}