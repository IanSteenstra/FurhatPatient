package furhatos.app.openaichat.flow.chatbot

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import furhatos.app.openaichat.setting.conditionType
import furhatos.flow.kotlin.DialogHistory
import furhatos.flow.kotlin.Furhat
import io.github.sashirestela.openai.SimpleOpenAI
import io.github.sashirestela.openai.common.ResponseFormat
import io.github.sashirestela.openai.domain.chat.ChatMessage
import io.github.sashirestela.openai.domain.chat.ChatRequest
import java.lang.System.getenv

/** Open AI API Key **/
val serviceKey = getenv("OPENAI_API_KEY") ?: throw IllegalStateException("API Key not found")

val openAI = SimpleOpenAI.builder()
    .apiKey(serviceKey)
    .build();

data class PADScores(var pleasure: Double = 0.0, var arousal: Double = 0.0, var dominance: Double = 0.0)
data class Beliefs(var belief1: String = "", var belief2: String = "", var belief3: String = "")
data class Desires(val desire1: String = "", val desire2: String = "", val desire3: String = "")
data class Intentions(val intention1: String = "", val intention2: String = "", val intention3: String = "")
data class EmotionsTop(val emotion1: String = "", val emotion1_intensity: Double = 0.0, val emotion2: String = "", val emotion2_intensity: Double = 0.0, val emotion3: String = "", val emotion3_intensity: Double = 0.0)
data class AppraisalVariables(
    val relevance_reasoning: String = "",
    val relevance: Double = 0.0,
    val desirability_reasoning: String = "",
    val desirability: Double = 0.0,
    val expectedness_reasoning: String = "",
    val expectedness: Double = 0.0,
    val likelihood_reasoning: String = "",
    val likelihood: Double = 0.0,
    val controllability_reasoning: String = "",
    val controllability: Double = 0.0,
    val changeability_reasoning: String = "",
    val changeability: Double = 0.0,
    val causal_attribution_reasoning: String = "",
    val causal_attribution: Double = 0.0
)

data class UpdatedBDI(
    val beliefs: Beliefs = Beliefs(),
    val desires: Desires = Desires(),
    val intentions: Intentions = Intentions()
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AppraisalSchema(
    val attention: String = "",
    val appraisal_variables: AppraisalVariables = AppraisalVariables(),
    val pad_scores: PADScores = PADScores(),
    val emotions_top: EmotionsTop = EmotionsTop(),
    val coping_potential_reasoning: String = "",
    val coping_potential: Double = 0.0,
    val coping_approach_reasoning: String = "",
    val coping_approach: String = "",
    val updated_bdi: UpdatedBDI = UpdatedBDI(),
    val next_patient_utterance_reasoning: String = "",
    val next_patient_utterance: String = "",
    val next_patient_facial_expression: String = "",
    val gesture: String = ""
)

val objectMapper = ObjectMapper()
val jsonSchema: ResponseFormat = ResponseFormat.jsonSchema(
    ResponseFormat.JsonSchema.builder()
        .name("appraisal_schema")
        .description("Schema for the appraisal data")
        .strict(true)
        .schemaClass(AppraisalSchema::class.java) // Keep this for type safety
        .build().also { schema ->  // Modify the base schema
            val schemaObject = objectMapper.createObjectNode().apply {
                put("type", "object")
                put("additionalProperties", false) // Forbid extra properties at the top level

                putObject("properties").also { properties -> // Define all properties within this block
                    // Simple properties
                    properties.putObject("attention").put("type", "string")

                    // Nested Objects
                    properties.putObject("appraisal_variables").put("type", "object").apply {
                        put("additionalProperties", false)
                        putObject("properties").also { appraisalVariablesProps ->
                            appraisalVariablesProps.putObject("relevance_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("relevance").put("type", "number")
                            appraisalVariablesProps.putObject("desirability_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("desirability").put("type", "number")
                            appraisalVariablesProps.putObject("expectedness_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("expectedness").put("type", "number")
                            appraisalVariablesProps.putObject("likelihood_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("likelihood").put("type", "number")
                            appraisalVariablesProps.putObject("controllability_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("controllability").put("type", "number")
                            appraisalVariablesProps.putObject("changeability_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("changeability").put("type", "number")
                            appraisalVariablesProps.putObject("causal_attribution_reasoning").put("type", "string")
                            appraisalVariablesProps.putObject("causal_attribution").put("type", "number")
                        }
                        putArray("required").apply {
                            add("relevance_reasoning")
                            add("relevance")
                            add("desirability_reasoning")
                            add("desirability")
                            add("expectedness_reasoning")
                            add("expectedness")
                            add("likelihood_reasoning")
                            add("likelihood")
                            add("controllability_reasoning")
                            add("controllability")
                            add("changeability_reasoning")
                            add("changeability")
                            add("causal_attribution_reasoning")
                            add("causal_attribution")
                        }
                    }

                    properties.putObject("pad_scores").put("type", "object").apply {
                        put("additionalProperties", false)  // Important: Disallow extra properties within pad_scores
                        putObject("properties").also { padScoresProperties ->
                            padScoresProperties.putObject("pleasure").put("type", "number")
                            padScoresProperties.putObject("arousal").put("type", "number")
                            padScoresProperties.putObject("dominance").put("type", "number")
                        }
                        putArray("required").apply {
                            add("pleasure")
                            add("arousal")
                            add("dominance")
                        }
                    }

                    properties.putObject("emotions_top").put("type", "object").apply {
                        put("additionalProperties", false)
                        putObject("properties").also { emotionsTopProps ->
                            emotionsTopProps.putObject("emotion1").put("type", "string")
                            emotionsTopProps.putObject("emotion1_intensity").put("type", "number")
                            emotionsTopProps.putObject("emotion2").put("type", "string")
                            emotionsTopProps.putObject("emotion2_intensity").put("type", "number")
                            emotionsTopProps.putObject("emotion3").put("type", "string")
                            emotionsTopProps.putObject("emotion3_intensity").put("type", "number")
                        }
                        putArray("required").apply {
                            add("emotion1")
                            add("emotion1_intensity")
                            add("emotion2")
                            add("emotion2_intensity")
                            add("emotion3")
                            add("emotion3_intensity")
                        }
                    }

                    properties.putObject("coping_potential_reasoning").put("type", "string")
                    properties.putObject("coping_potential").put("type", "number")
                    properties.putObject("coping_approach_reasoning").put("type", "string")
                    properties.putObject("coping_approach").put("type", "string")

                    properties.putObject("updated_bdi").put("type", "object").apply {
                        put("additionalProperties", false)
                        putObject("properties").also { updatedBdiProps ->
                            updatedBdiProps.putObject("beliefs").put("type", "object").apply {
                                put("additionalProperties", false)
                                putObject("properties").also { beliefsProps ->
                                    beliefsProps.putObject("belief1").put("type", "string")
                                    beliefsProps.putObject("belief2").put("type", "string")
                                    beliefsProps.putObject("belief3").put("type", "string")
                                }
                                putArray("required").apply {
                                    add("belief1")
                                    add("belief2")
                                    add("belief3")
                                }
                            }

                            updatedBdiProps.putObject("desires").put("type", "object").apply {
                                put("additionalProperties", false)
                                putObject("properties").also { desiresProps ->
                                    desiresProps.putObject("desire1").put("type", "string")
                                    desiresProps.putObject("desire2").put("type", "string")
                                    desiresProps.putObject("desire3").put("type", "string")
                                }
                                putArray("required").apply {
                                    add("desire1")
                                    add("desire2")
                                    add("desire3")
                                }
                            }

                            updatedBdiProps.putObject("intentions").put("type", "object").apply {
                                put("additionalProperties", false)
                                putObject("properties").also { intentionsProps ->
                                    intentionsProps.putObject("intention1").put("type", "string")
                                    intentionsProps.putObject("intention2").put("type", "string")
                                    intentionsProps.putObject("intention3").put("type", "string")
                                }
                                putArray("required").apply {
                                    add("intention1")
                                    add("intention2")
                                    add("intention3")
                                }
                            }
                        }
                        putArray("required").apply {
                            add("beliefs")
                            add("desires")
                            add("intentions")
                        }
                    }

                    properties.putObject("next_patient_utterance_reasoning").put("type", "string")
                    properties.putObject("next_patient_utterance").put("type", "string")
                    properties.putObject("next_patient_facial_expression").put("type", "string")

                    putArray("required").apply {
                        add("attention")
                        add("appraisal_variables")
                        add("pad_scores")
                        add("emotions_top")
                        add("coping_potential_reasoning")
                        add("coping_potential")
                        add("coping_approach_reasoning")
                        add("coping_approach")
                        add("updated_bdi")
                        add("next_patient_utterance_reasoning")
                        add("next_patient_utterance")
                        add("next_patient_facial_expression")
                    }
                } // properties.also{} block
            } // schemaObject.apply{}

            val schemaField = ResponseFormat.JsonSchema::class.java.getDeclaredField("schema")
            schemaField.isAccessible = true
            schemaField.set(schema, schemaObject)
        }
)

class OpenAIChatbot() {

    // TODO: Modify for idle or intervention option
    fun getResponse(Prompt: String): Any? {
        val chatRequestBuilder = ChatRequest.builder()
            .model("gpt-4o-mini")
            .message(ChatMessage.SystemMessage.of(Prompt))

        if (conditionType == "intervention") {
            chatRequestBuilder.responseFormat(jsonSchema)
        }
        Furhat.dialogHistory.all.takeLast(10).forEach {
            when (it) {
                is DialogHistory.ResponseItem -> {
                    chatRequestBuilder.message(ChatMessage.UserMessage.of(it.response.text))
                }

                is DialogHistory.UtteranceItem -> {
                    chatRequestBuilder.message(ChatMessage.AssistantMessage.of(it.toText()))
                }
            }
        }

        val futureChat = openAI.chatCompletions().create(chatRequestBuilder.build())
        val chatResponse = futureChat.join()
        val jsonResponse = chatResponse.firstContent().toString()

        println(jsonResponse)

        // Deserialize the JSON string into AppraisalSchema object
        if (conditionType == "intervention") {
            println(objectMapper.readValue(jsonResponse, AppraisalSchema::class.java))
            return objectMapper.readValue(jsonResponse, AppraisalSchema::class.java)
        } else {
            return jsonResponse
        }
    }
}