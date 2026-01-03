# JSON Parsing Performance Benchmark (Large Data)

이 프로젝트는 Java 환경에서 대용량 JSON 데이터를 처리할 때 사용되는 주요 라이브러리들의 성능을 **JMH(Java Microbenchmark Harness)**를 통해 정밀하게 비교 분석합니다.

## 🛠 Tech Stack
- **Language**: Java 17
- **Build Tool**: Gradle 8.x
- **Benchmark**: JMH 1.37
- **Libraries**:
  - [Jackson](https://github.com/FasterXML/jackson) (v2.16.2)
  - [Jsoniter](http://jsoniter.com/) (v0.9.23)
  - [DslJson](https://github.com/ngs-no-stress/dsl-json) (v1.10.0)

## 📊 Benchmark Environment
- **JVM**: Java HotSpot(TM) 64-Bit Server VM, 17.0.8+9-LTS-211
- **Warmup**: 5 Iterations, 10s each
- **Measurement**: 10 Iterations, 10s each
- **Data**: 약 10,000개의 객체를 포함한 대용량 JSON (FrameData 모델)
- **Metric**: Throughput (ops/ms - 밀리초당 처리 횟수, **높을수록 좋음**)

## 📈 Benchmark Results

| Rank | Method | Library | Strategy | Score (ops/ms) | Error |
|:---:|:---|:---|:---|:---:|:---:|
| 1 | `e_jsoniterAny` | **Jsoniter** | Any (Dynamic) | **0.2244** | ± 0.0054 |
| 2 | `a_dslJson` | **DslJson** | Type Bind | **0.0306** | ± 0.0008 |
| 3 | `f_jacksonJsonNode` | **Jackson** | JsonNode | 0.0180 | ± 0.0001 |
| 4 | `d_jsoniter` | **Jsoniter** | Type Bind | 0.0178 | ± 0.0003 |
| 5 | `b_jacksonSingleton` | **Jackson** | Singleton | 0.0176 | ± 0.0004 |
| 6 | `c_jacksonNewInstance` | **Jackson** | New Instance | 0.0176 | ± 0.0005 |

### 🔍 분석 결과 요약
1.  **동적 파싱의 압도적 속도**: `Jsoniter Any` 방식이 타입 바인딩 방식보다 **약 7배~12배** 빠른 성능을 보였습니다. 스키마 전체를 객체로 변환하지 않고 필요한 부분만 탐색하는 방식의 효율성을 입증합니다.
2.  **DslJson의 우위**: 엄격한 타입 바인딩(`POJO` 변환) 방식 중에서는 `DslJson`이 Jackson보다 **약 1.7배** 더 높은 처리량을 기록했습니다.
3.  **Jackson의 인스턴스 생성 비용**: Jackson의 `Singleton` 사용과 `New Instance` 생성 간의 성능 차이는 이번 대용량 테스트 환경에서 매우 미미한 것으로 나타났습니다.
4.  **JsonNode vs POJO**: Jackson 사용 시 대용량 데이터에 대해서는 POJO 매핑보다 `JsonNode`를 통한 접근이 근소하게 빠른 성능을 보였습니다.

## 🚀 How to Run

### Gradle 명령어로 실행
터미널에서 아래 명령어를 실행하면 전체 벤치마크가 수행되고 결과가 JSON 파일로 생성됩니다.


bash ./gradlew jmh
IDE(IntelliJ)에서 실행 및 프로파일링
`JmhRunner.java`를 실행하여 직접 테스트를 수행할 수 있습니다. **VisualVM** 등의 툴을 사용하여 런타임 중의 CPU 및 Memory 점유율을 함께 분석하는 것을 권장합니다.

```
java // JmhRunner.java public class JmhRunner { public static void main(String[] args) throws Exception { Options opt = new OptionsBuilder() .include(".JsonBenchmarkLarge.") .forks(2) .build(); new Runner(opt).run(); } }
``` 

## 📂 Project Structure
- `src/main/java/com/eastwoo/study/JmhRunner.java`: 벤치마크 실행기
- `src/main/java/com/eastwoo/study/JsonBenchmarkLarge.java`: 메인 벤치마크 로직
- `src/main/java/com/eastwoo/study/LargeJsonGenerator.java`: 테스트용 대용량 데이터 생성기
- `src/main/java/com/eastwoo/study/dto/FrameData.java`: JSON 데이터 매핑을 위한 모델

