# LogisticsExceptionHandlingAgent

物流异常电联智能处理 Agent——以圆准达「异常上报 + 电联意图识别打标」流程为业务蓝本，将外部黑盒打标升级为具备意图识别、业务工具调用、规则检索、人工审核与效果评估能力的 Agent 闭环。

## 当前进度：阶段 1 已实现

当前版本完成电联转写文本到 Racking 八类意图的结构化识别，提供可启动的 REST 服务：

- 八类标签业务契约：1 上门、2 入库入柜、3 指定地点、4 引导推诿、5 系统音、6 仅一方发言、7 语义混乱、8 与快递无关。
- 结构化输出：标签、置信度、原文证据句、配送要素、人工审核标记和判断原因。
- 结果硬校验：非法标签直接拒绝；证据句必须逐字摘自原文；置信度低于阈值强制转人工。
- 两种 Provider：`openai` 使用 Spring AI 调用 OpenAI 兼容大模型；`mock` 使用确定性演示规则，便于无密钥运行和测试。
- 原系统动作映射契约：仅 value=2 对应 `IN_STATION`，其余对应 `resetCheck`。

Tool Calling、RAG、工作流持久化、MQ 接入和人工审核台属于后续阶段，当前版本不包含。

## 模块结构

| 模块 | 当前状态 | 职责 |
|------|----------|------|
| `backend/agent-common` | 已实现 | Racking 标签、CheckType 映射、识别命令与结构化结果 |
| `backend/agent-core` | 已实现 | Prompt、Spring AI 识别、Mock Provider、结果校验 |
| `backend/agent-admin` | 已实现 | Spring Boot 启动应用、意图识别 REST API |
| `backend/agent-business` | 阶段 2 | 运单、轨迹、网点、偏好和工单 Mock 业务服务 |
| `backend/agent-consumer` | 阶段 4 | MQ 消费与异步任务接入 |
| `frontend` | 阶段 5 | 执行轨迹与人工审核台 |

技术版本：Java 21、Spring Boot 3.4.5、Spring AI 1.0.0、Maven 多模块。

## 构建与测试

本机 Maven 默认指向 JDK 8，Git Bash 中需显式切换到 JDK 21：

```bash
export JAVA_HOME='D:\workspace\tools\jdk-21'
export PATH="$JAVA_HOME/bin:$PATH"
mvn clean package
```

## 本地启动：Mock Provider

```bash
java -jar backend/agent-admin/target/agent-admin-0.1.0-SNAPSHOT.jar
```

另开终端调用接口：

```bash
curl -X POST 'http://localhost:8080/api/v1/intent/recognize' \
  -H 'Content-Type: application/json; charset=UTF-8' \
  --data-binary '@examples/intent-request.json'
```

Git Bash 内联中文可能产生编码问题，因此示例使用 UTF-8 JSON 文件。正常响应：

```json
{
  "tagCode": "Racking",
  "value": "3",
  "intentName": "指定地点",
  "confidence": 0.93,
  "contextSentence": "我白天不在家，把快递放小区门卫",
  "extracted": {
    "locationType": "门卫",
    "locationDetail": "小区门卫",
    "timePreference": "白天不在家"
  },
  "needHumanReview": false,
  "reason": "本地 Mock 根据演示语料关键词分类"
}
```

## 接入真实大模型

设置环境变量后使用同一个 JAR 启动：

```bash
export INTENT_PROVIDER=openai
export LLM_BASE_URL='http://your-openai-compatible-host:port'
export LLM_API_KEY='替换为真实密钥'
export LLM_MODEL='qwen-plus'
export INTENT_CONFIDENCE_THRESHOLD=0.75
java -jar backend/agent-admin/target/agent-admin-0.1.0-SNAPSHOT.jar
```

`LLM_BASE_URL` 和模型渠道前缀可按实际 OpenAI 兼容服务调整。密钥只通过环境变量传入，不写入仓库。

## API

`POST /api/v1/intent/recognize`

| 字段 | 约束 | 说明 |
|------|------|------|
| `waybillNo` | 非空，最长 64 | 运单号 |
| `callId` | 非空，最长 128 | 电联编号 |
| `transcript` | 非空，最长 20000 | 通话转写文本 |

参数错误返回 400；模型调用失败或模型结果未通过契约校验返回 502。

## 文档与后续阶段

- [01-设计方案](docs/01-设计方案.md)：业务现状、目标流程、完整架构和五阶段计划。
- 阶段 2：业务工具与 Tool Calling。
- 阶段 3：pgvector 配送规则知识库。
- 阶段 4：工作流状态机、人工审核、重试与中断恢复。
- 阶段 5：评估数据集、Langfuse、Docker Compose 和 React 审核台。
