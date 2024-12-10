package furhatos.app.openaichat.flow.chatbot

import com.fasterxml.jackson.annotation.JsonInclude
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

data class PADScores(val Pleasure: Double = 0.0, val Arousal: Double = 0.0, val Dominance: Double = 0.0)
data class Beliefs(val Belief1: String = "", val Belief2: String = "", val Belief3: String = "")
data class Desires(val Desire1: String = "", val Desire2: String = "", val Desire3: String = "")
data class Intentions(val Intention1: String = "", val Intention2: String = "", val Intention3: String = "")
data class EmotionsTop(val emotion1: String = "", val emotion1Intensity: Double = 0.0, val emotion2: String = "", val emotion2Intensity: Double = 0.0, val emotion3: String = "", val emotion3Intensity: Double = 0.0)
data class AppraisalVariables(
    val Relevance: Double = 0.0,
    val RelevanceReasoning: String = "",
    val Desirability: Double = 0.0,
    val DesirabilityReasoning: String = "",
    val Expectedness: Double = 0.0,
    val ExpectednessReasoning: String = "",
    val Likelihood: Double = 0.0,
    val LikelihoodReasoning: String = "",
    val Controllability: Double = 0.0,
    val ControllabilityReasoning: String = "",
    val Changeability: Double = 0.0,
    val ChangeabilityReasoning: String = "",
    val CausalAttribution: Double = 0.0,
    val CausalAttributionReasoning: String = ""
)

data class UpdatedBDI(
    val Beliefs: Beliefs = Beliefs(),
    val Desires: Desires = Desires(),
    val Intentions: Intentions = Intentions()
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
    val next_patient_facial_expression: String = ""
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
                            appraisalVariablesProps.putObject("RelevanceReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("Relevance").put("type", "number")
                            appraisalVariablesProps.putObject("DesirabilityReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("Desirability").put("type", "number")
                            appraisalVariablesProps.putObject("ExpectednessReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("Expectedness").put("type", "number")
                            appraisalVariablesProps.putObject("LikelihoodReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("Likelihood").put("type", "number")
                            appraisalVariablesProps.putObject("ControllabilityReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("Controllability").put("type", "number")
                            appraisalVariablesProps.putObject("ChangeabilityReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("Changeability").put("type", "number")
                            appraisalVariablesProps.putObject("CausalAttributionReasoning").put("type", "string")
                            appraisalVariablesProps.putObject("CausalAttribution").put("type", "number")
                        }
                        putArray("required").apply {
                            add("RelevanceReasoning")
                            add("Relevance")
                            add("DesirabilityReasoning")
                            add("Desirability")
                            add("ExpectednessReasoning")
                            add("Expectedness")
                            add("LikelihoodReasoning")
                            add("Likelihood")
                            add("ControllabilityReasoning")
                            add("Controllability")
                            add("ChangeabilityReasoning")
                            add("Changeability")
                            add("CausalAttributionReasoning")
                            add("CausalAttribution")
                        }
                    }

                    properties.putObject("pad_scores").put("type", "object").apply {
                        put("additionalProperties", false)  // Important: Disallow extra properties within pad_scores
                        putObject("properties").also { padScoresProperties ->
                            padScoresProperties.putObject("Pleasure").put("type", "number")
                            padScoresProperties.putObject("Arousal").put("type", "number")
                            padScoresProperties.putObject("Dominance").put("type", "number")
                        }
                        putArray("required").apply {
                            add("Pleasure")
                            add("Arousal")
                            add("Dominance")
                        }
                    }

                    properties.putObject("emotions_top").put("type", "object").apply {
                        put("additionalProperties", false)
                        putObject("properties").also { emotionsTopProps ->
                            emotionsTopProps.putObject("emotion1").put("type", "string")
                            emotionsTopProps.putObject("emotion1Intensity").put("type", "number")
                            emotionsTopProps.putObject("emotion2").put("type", "string")
                            emotionsTopProps.putObject("emotion2Intensity").put("type", "number")
                            emotionsTopProps.putObject("emotion3").put("type", "string")
                            emotionsTopProps.putObject("emotion3Intensity").put("type", "number")
                        }
                        putArray("required").apply {
                            add("emotion1")
                            add("emotion1Intensity")
                            add("emotion2")
                            add("emotion2Intensity")
                            add("emotion3")
                            add("emotion3Intensity")
                        }
                    }

                    properties.putObject("coping_potential_reasoning").put("type", "string")
                    properties.putObject("coping_potential").put("type", "number")
                    properties.putObject("coping_approach_reasoning").put("type", "string")
                    properties.putObject("coping_approach").put("type", "string")

                    properties.putObject("updated_bdi").put("type", "object").apply {
                        put("additionalProperties", false)
                        putObject("properties").also { updatedBdiProps ->
                            updatedBdiProps.putObject("Beliefs").put("type", "object").apply {
                                put("additionalProperties", false)
                                putObject("properties").also { beliefsProps ->
                                    beliefsProps.putObject("Belief1").put("type", "string")
                                    beliefsProps.putObject("Belief2").put("type", "string")
                                    beliefsProps.putObject("Belief3").put("type", "string")
                                }
                                putArray("required").apply {
                                    add("Belief1")
                                    add("Belief2")
                                    add("Belief3")
                                }
                            }

                            updatedBdiProps.putObject("Desires").put("type", "object").apply {
                                put("additionalProperties", false)
                                putObject("properties").also { desiresProps ->
                                    desiresProps.putObject("Desire1").put("type", "string")
                                    desiresProps.putObject("Desire2").put("type", "string")
                                    desiresProps.putObject("Desire3").put("type", "string")
                                }
                                putArray("required").apply {
                                    add("Desire1")
                                    add("Desire2")
                                    add("Desire3")
                                }
                            }

                            updatedBdiProps.putObject("Intentions").put("type", "object").apply {
                                put("additionalProperties", false)
                                putObject("properties").also { intentionsProps ->
                                    intentionsProps.putObject("Intention1").put("type", "string")
                                    intentionsProps.putObject("Intention2").put("type", "string")
                                    intentionsProps.putObject("Intention3").put("type", "string")
                                }
                                putArray("required").apply {
                                    add("Intention1")
                                    add("Intention2")
                                    add("Intention3")
                                }
                            }
                        }
                        putArray("required").apply {
                            add("Beliefs")
                            add("Desires")
                            add("Intentions")
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
            return objectMapper.readValue(jsonResponse, AppraisalSchema::class.java)
        } else {
            return jsonResponse
        }
    }
}