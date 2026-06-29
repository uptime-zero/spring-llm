# Spring LLM Chatbot

Spring AI 2.0 + Ollama 기반 로컬 챗봇 프로젝트입니다.

## 기술 스택

| 항목 | 버전 |
|------|------|
| Java | 25 LTS |
| Spring Boot | 4.1.0 |
| Spring AI | 2.0.0 |
| Gradle | 9.6 |
| Ollama | 최신 |

## 실행 전 요구사항

```bash
java -version        # openjdk 25 이상
ollama pull gemma3:1b
ollama pull llama3.1
ollama serve
```

## 실행

```bash
./gradlew bootRun
```

서버 기동 후 `http://localhost:8080` 접속

## 주요 기능

- **SSE 스트리밍** — 실시간 응답 스트리밍 (`/api/chat/stream`)
- **대화 메모리** — 세션별 대화 이력 유지 (최대 20개 메시지)
- **모델 전환** — 요청마다 `gemma3:1b` / `llama3.1` 선택 가능
- **모델별 페르소나** — 모델 능력에 맞는 시스템 프롬프트 자동 선택
- **MDC 구조화 로깅** — 요청 추적을 위한 conversationId 로그 태깅

## 페르소나

무면허 사설탐정 **강무진** 캐릭터를 주입합니다.

| 모델 | 페르소나 버전 |
|------|--------------|
| `llama3.1` | 프로필 + 지시사항 포함 상세 버전 |
| `gemma3:1b` | 1B 모델 한계를 고려한 경량 버전 |

## API

```bash
# 기본 스트리밍 (gemma3:1b)
curl -N -X POST http://localhost:8080/api/chat/stream \
  -H "Content-Type: application/json" \
  -d '{"message":"안녕","conversationId":"s1"}'

# 모델 지정 (llama3.1)
curl -N -X POST http://localhost:8080/api/chat/stream \
  -H "Content-Type: application/json" \
  -d '{"message":"자기소개 해줘","conversationId":"s1","model":"llama3.1"}'
```

## 설정

`src/main/resources/application.yaml`에서 모델·온도·페르소나 등을 조정합니다.

```yaml
chatbot:
  client:
    model: gemma3:1b
    temperature: 0.6
    num-predict: 3200
  personas:
    full:   # llama3.1 전용
      ...
    lite:   # gemma3:1b 전용
      ...
```
