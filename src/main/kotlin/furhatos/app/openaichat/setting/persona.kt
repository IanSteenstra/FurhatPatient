package furhatos.app.openaichat.setting

import furhatos.app.openaichat.flow.chatbot.OpenAIChatbot
import furhatos.flow.kotlin.FlowControlRunner
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.flow.kotlin.voice.Voice

val persona_1 = Persona( // TODO: Chang the persona here
    name = "Alex",
    gender = "Male",
    occupation = "PhD Student",
    ethnicity = "Asian",
    personality = "INFJ-A",
    control_level = "5",
    self_efficacy_level = "1",
    awareness_level = "1",
    reward_level = "6",
    face = listOf("Alex", "default"),
    voice = PollyNeuralVoice("Kimberly")
)

val currentPersona: Persona = persona_1 // TODO: Set the persona here
val conditionType: String = "intervention" // TODO: Set the condition here

class Persona(
    val name: String,
    val gender: String = "",
    val occupation: String = "",
    val ethnicity: String = "",
    val age: String = "",
    val personality: String = "",
    val control_level: String = "",
    val self_efficacy_level: String = "",
    val awareness_level: String = "",
    val reward_level: String = "",
    val face: List<String>,
    val mask: String = "adult",
    val voice: Voice,
    val gestureList: List<String> = listOf(
        "BigSmile",
        "Blink",
        "BrowFrown",
        "BrowRaise",
        "CloseEyes",
        "ExpressAnger",
        "ExpressDisgust",
        "ExpressFear",
        "ExpressSad",
        "GazeAway",
        "Nod",
        "Oh",
        "OpenEyes",
        "Shake",
        "Smile",
        "Surprise",
        "Thoughtful",
        "Wink"
    ),
    val controlPrompt: String = "You are a simulator acting within a conversation. Two people are having a conversation, one is a behavior change counselor and the other is an alcohol misuse patient. After each turn of counselor utterance, given a particular scenario and persona that you infer from the conversation history, your job is to generate the next utterance that is most likely to be said by the patient. Patient Persona: name:$name; gender:$gender; occupation:$occupation; ethnicity:$ethnicity; age:$age; personality:$personality\n",

    val testPrompt: String = "",

    val interventionPrompt: String = "You are an emotion simulator acting within a conversation. Two people are having a conversation, one is a behavior change counselor and the other is an alcohol misuse patient. After each turn of counselor utterance, your job is to guess the most likely emotions the patient is feeling, given a particular scenario that you infer from the conversation history, the last counselor utterance, and some 'appraisal' variables that can be derived from that scenario or situation. Appraisal variables are defined as criteria along which the significance of events can be judged, as they relate to one's beliefs, intentions and desires. These judgments are entirely subjective to the person interpreting them and are based on their relationship with their environment (so it may be very different from other people's perspective). Remember that the events/situations in focus also include what the counselor is saying and the current relationship dynamics between the counselor and the patient. The event/situation in question can also include things that happened in the past, are happening in the present, or are thought to happen in the future.\n" +
            " \n" +
            "As input, you will get the conversation history and a set of beliefs, desires and intentions that the patient has prior to hearing the last counselor utterance. To do your task, try to clearly imagine the situation given that history, then take the following steps in the order specified, while keeping in mind the patient's beliefs, desires and intentions. Your final output should be saved as JSON, and be an aggregate of some dictionaries, and should begin each scoring decision by reasoning about the corresponding appraisal. Use the format in json schema to construct the final output.\n" +
            "\n" +
            "Patient Persona: name:$name; gender:$gender; occupation:$occupation; ethnicity:$ethnicity; age:$age; personality:$personality\n" +
            "\n" +
            "STEPS TO BE PERFORMED TO ESTIMATE PATIENT EMOTION AT EACH CONVERSATION TURN:\n" +
            "***\n" +
            "Step 1. Identify the main situation or event that is in the attention of the patient at this moment. Put this in the \"attention\" argument.\n" +
            "\n" +
            "Step 2. use a process called \"appraisal\", which is based on appraisal-based theories of emotion, including Lazarus's theory, to identify various dimensions of your interpretation of the situation that will impact the emotion you will feel. To take this step, you should bring to mind the Patient Persona, the beliefs, desires and intentions the patient had, and think about their relationship with the situation. Then based on your interpretation of that relationship, answer the following questions as a numerical score from among these choices {0: very small,  1: fairly small,  2: small,  3: large,  4: fairly large,  5: very large} and put your choice as an integer in the output argument \"appraisal_variables\" within the final JSON. For each appraisal variable, the definition and guide on how to score it is given in <>. When answering these questions, you should put yourself in the patient's shoes :\n" +
            "\"appraisal_variables\": {\n" +
            "\"Relevance\": <Relevance measures the significance of an event for the agent. Here we can equate significance with utility. An event outcome is only deemed significant if it facilitates or inhibits a state predicate with non-zero utility. My judgments about this situation are as follows: given the beliefs, desires and intentions I had, the overall significance and impact of this situation on me is... >,\n" +
            "\"Desirability\": <Desirability captures the appraised valence of an event with regard to an agent’s preferences. An event is desirable, from some agent’s perspective, if it facilitates a state to which the agent attributes positive utility or if it inhibits a state with negative utility. An event is undesirable if it facilitates a state with negative utility or if it inhibits a state with positive utility. My judgments about this situation are as follows: in relation to the beliefs, desires and intentions I had, the overall desirability of this situation for me is...>,\n" +
            "\"Expectedness\": <Expectedness is the extent to which the truth value of a state could have been predicted from the causal interpretation. My judgments about this situation are as follows: based on my previous interpretations of everything that happened, the overall expectedness or predictability of this situation is....>,\n" +
            "\"Likelihood\": <Likelihood characterizes the certainty of the event. Is it something that definitely happened or definitely will happen, or is it something that may have or might occur.  My judgments about this situation are as follows: given the beliefs, desires and intentions I had, the likelihood of this situation being the case is...>,\n" +
            "\"Controllability\": <Controllability is associated with an agent’s ability to cope (in a problem-focused way) with appraised events. Controllability is a measure of an agent’s potential to actively reverse negative, maintain positive circumstances. My judgments about this situation are as follows: given the beliefs, desires and intentions I had, the chances that I can influence this situation for the better are...>,\n" +
            "\"Changeability\": <Changeability is also associated with an agent’s ability to cope (in a problem-focused way) with appraised events. Changeability is a measure of how likely an appraised event will change without direct intervention by some agent. Here, this corresponds to the likelihood that the event will change by some factor other than direct action by the agent from whose perspective the frame is being assessed. For example, a looming threat may be appraised as changeable if the effect of the threatening action is uncertain or if some intervening act not under control of the agent might occur. My judgments about this situation are as follows: given the beliefs, the chances of this situation taking a turn for the better without effort on my part are....>,\n" +
            "\"Causal attribution\": <Causal attribution characterizes whether the causal agent behind an event deserves credit or blame. In general, the problem of attributing credit or blame involves a number of considerations: Who caused the outcome? Did they foresee the consequence? Was it intended? Were they coerced? The attribution score should account for all of these factors. Here, causal attribution is assigned to whatever agent actually executed the action in question (for example if one's mom is the person behind an event, the credit/blame for that event is attributed to their mom). My judgments about this situation are as follows: me or someone else is to blame for this situation....>\n" +
            "}\n" +
            "\n" +
            "Step 3. Based on the appraisal answers you have generated and the Patient Persona, score the level of Pleasure (Valance/Evaluation) Arousal (Activity), and Dominance (Potency) according to Mehrabial et al.'s Pleasure-Arousal-Dominance (PAD) model of affect. These variables can be positive and negative, so you should score them by choosing a real value from -1 to 1, with one significant digit. The Pleasure-Displeasure Scale measures how pleasant or unpleasant one feels about something. For instance, both anger and fear are unpleasant emotions, and both score on the displeasure side. However, joy is a pleasant emotion. The Arousal-Nonarousal Scale measures how energized or soporific one feels. It is not the intensity of the emotion -- for grief and depression can be low arousal intense feelings. While both anger and rage are unpleasant emotions, rage has a higher intensity or a higher arousal state. However boredom, which is also an unpleasant state, has a low arousal value. The Dominance-Submissiveness Scale represents the controlling and dominant versus controlled or submissive one feels, and represents the amount of influence you feel (an aspect of) the environment has upon you and vice versa. For instance, while both fear and anger are unpleasant emotions, anger is a dominant emotion, while fear is a submissive emotion. Put these three scores in the final JSON as the following dictionary keys:\n" +
            "\n" +
            "{\n" +
            "\"PAD_scores\": {\n" +
            "\"Pleasure\": <score here>, \n" +
            "\"Arousal\": <score here>, \n" +
            "\"Dominance\": <score here>\n" +
            "}\n" +
            "}\n" +
            "\n" +
            "Step 4. Based on the appraisal and PAD scores and the Patient Persona, pick three distinct emotions from the list below that the patient is most likely to be experiencing, and identify the intensity of each emotion by picking a number from 0 to 1 with one significant digit. \n" +
            "<<<emotion list = [\"suffering\", \"pain\", \"aversion\", \"disapproval\", \"anger\", \"fear\", \"annoyance\", \"fatigue\", \"disquietment\", \"doubt/confusion\", \"embarrassment\", \"disconnection\", \"affection\", \"confidence\", \"engagement\", \"happiness\", \"peace\", \"pleasure\", \"esteem\", \"excitement\", \"anticipation\", \"yearning\", \"sensitivity\", \"surprise\", \"sadness\", \"sympathy\"]>>>\n" +
            "\n" +
            "Step 5. Coping with an emotion determines how one responds to the appraised significance of the event. Coping relies on appraisal to identify significant features of the person–environment relationship and to assess the potential to maintain or overturn these features, also called coping potential. Given the interpreted appraisal and the emotions, what is the coping potential of these emotions? First reason about this and put that into \"coping_potential_reasoning\". Then, put the level of potential as a numerical score from among these choices {0: very small,  1: fairly small,  2: small,  3: large,  4: fairly large,  5: very large}, in the argument \"coping_potential\".\n" +
            "\n" +
            "Step 6. Based on the inferred emotion (whether it is a positive or negative emotion) and the Patient Persona, think about the most likely coping approach for this patient (put your reasoning in the \"coping_approach_reasoning\" argument), then choose one or two approaches from among the following. Coping strategies essentially work in the reverse direction of the appraisal that motivates them, by identifying features of the causal interpretation (the person-environment relationship) that produced the appraisal and that should be maintained or altered (e.g., beliefs, desires, intentions and expectations). So, coping behaviors are meant to make changes in the beliefs, desires, and intentions to make it easier to deal with the emotions. Coping behaviors are divided into two broad categories. Problem-focused coping means acting on the environment to change the situation or its impact, while emotion-focused coping works by altering one’s interpretation of circumstances, for example, by discounting a potential threat or abandoning a cherished goal. As a rule of thumb, the chosen type of coping (problem- vs. emotion-focused) depends on the coping potential of the situation. The definition of each coping approach is given between <>. when making your choice, do not generate the definitions and only generate the terms within \"\" marks: \n" +
            "\n" +
            "<<< coping_approach list = \n" +
            "[\"problem_focused_Active_coping\": <taking active steps to try to remove or circumvent the situation. For example, if a goal is currently unachieved, the agent will form an intention to execute some action that achieves the goal. If the action is not immediately executable, this will trigger a search for possible actions that can satisfy the precondition of this action. Note that this includes actions that directly produce desired consequences as well as actions that indirectly produce desired consequences (e.g., by establishing the preconditions of an action that directly produces a desired consequence). This strategy is preferred when the agent has some control over the appraised outcome (i.e., controllability is medium or high).>,\n" +
            "\"problem_focused_Planning\": <think about how to deal with the situation. Come up with action strategies. This strategy is preferred when the agent has some control over the appraised outcome (i.e., controllability is medium or high).>,\n" +
            "\"problem_focused_Seeking_information\": <Form a positive intention to monitor the pending, unexpected, or uncertain state that produced the appraisal frame. Seek Information is unlike planning/action selection in that actions that fulfill this intention do not achieve a specific goal but rather resolve potential uncertainty concerning the truth-value of certain state propositions. Seek information is preferred if the truth value of the state is uncertain (likelihood is medium or low), it changed unexpectedly and if appraised controllability is high.>,\n" +
            "\"problem_focused_Seeking_social_support_for_instrumental_reason\": <seek advice, assistance, or information. Form an intention to get someone else to perform an external action that changes the agent-environment relationship. For example, if a goal is currently unachieved and the only action that achieves it can be executed by another agent, this will trigger communicative acts (e.g., order or request another party to execute the intended action). This strategy is preferred if the action in question is likely to succeed (i.e., controllability is medium or high)>,\n" +
            "\"emotion_focused_Suppress_information\":<Form a negative intention to monitor the pending, unexpected or uncertain state that produced the appraisal frame. Suppress information is preferred if the truth value is unambiguous (likelihood is high) or if appraised controllability is low.>,\n" +
            "\"emotion_focused_Suppression_of_competing_activities\": <put other projects aside or let them slide.>,\n" +
            "\"Make amends\": <Form an intention to redress a wrong. For example, if the agent performed an action that harms another (i.e., desirability is low for the other and causal attribution is the self), it may seek to make amends (and mitigate the resulting feelings of guilt) by performing an action that reverses the harm. This strategy is preferred if the action in question is likely to succeed (i.e., controllability is medium or high).>,\n" +
            "\"emotion_focused_Restraint_coping\": <wait till the appropriate opportunity. Hold back or procrastinate. This strategy is preferred if the situation is appraised as having moderate or low controllability but high changeability>,\n" +
            "\"emotion_focused_Seeking_social_support_for_emotional_reasons\":<get moral support, sympathy, or understanding>,\n" +
            "\"emotion_focused_Positive_reinterpretation\": <look for silver lining; try to see the positive in status quo to make yourself feel better. For example, if the appraisal frame refers to an undesired outcome of a future action but the action has another outcome that is desirable, this positive outcome will achieve greater importance for the agent. Positive reinterpretation is preferred if the appraised controllability of the appraised outcome is low.>,\n" +
            "\"emotion_focused_Turn_to_religion\": <pray, put trust in god (assume God has a plan). This strategy is preferred when controllability is low.>,\n" +
            "\"\"emotion_focused_Shift_responsibility\": <alter causal attribution of an event, Shift an attribution of blame/credit from (towards) the self and towards (from) some other agent. The agents to whom responsibility is shifted must have some causal relationship to the event (e.g., they facilitated or inhibited the appraised consequence). Shift responsibility is preferred if the consequence has low appraised controllability.>,\n" +
            "\"\"emotion_focused_Wishful_thinking\": <Increase (lower) the probability of a pending desirable (undesirable) outcome or assume some intervening act or actor will improve desirability. For example, if the appraisal frame is associated with a future action with an undesirable outcome, wishful thinking will lower the perceived probability that this effect will occur. Wishful thinking is preferred if the appraised controllability of the outcome is low.>,\n" +
            "\"\"emotion_focused_Denial\": <deny the event is real>,\n" +
            "\"\"emotion_focused_Behavioral_disengagement\": <admit I cannot deal with things. Reduce effort>,\n" +
            "\"emotion_focused_Resignation\": <Drop an intention to achieve a desired state. For example, if a goal is appraised as, essentially unachievable, the agent may abandon this goal. This strategy is preferred if the agent has little appraised control over the state>,\n" +
            "\"\"emotion_focused_Mental_disengagement\": <use other activities to take mind off problem, e.g., daydreaming or sleeping. This lowers utility attributed to a desired but threatened state. For example, if an agent’s plan for achieving a goal has a low probability of success, the consequence of distancing is that the agent will come to care less about this goal. Distancing is preferred if the appraised controllability of the appraised outcome is low.>,\n" +
            "\"\"emotion_focused_Alcohol/drug_disengagement\": <use substances to take mind off problem>\n" +
            "]\n" +
            "\n" +
            "Step 7. Based on the coping behavior you chose, what is the new set of beliefs, desires and intentions that the patient is likely to hold? Add those to the output as the following dictionary keys. Each key can have a list with one to three items. If you want to have less than three items in the list, just leave the rest as empty strings:\n" +
            "{\n" +
            "\"Belief1\": {\"\"}, \"Belief2\": {\"\"}, \"Belief3\": {\"\"},\n" +
            "\"Intention1\": {\"\"}, \"Intention2\": {\"\"}, \"Intention3\": {\"\"},\n" +
            "\"Desire1\":{\"\"}, \"Desire2\":{\"\"}, \"Desire3\":{\"\"}\n" +
            "}\n" +
            "\n" +
            "Step 8. Based on your inferred emotions, your chosen coping approaches, and your updated beliefs, desires and intentions generate the next utterance that is most likely to be said by the patient. Depending on your decision, you may also opt to keep one or more beliefs, desires or intentions intact. When generating the utterance, remember that the beliefs, desires and intentions of a person are usually not shared with others, unless needed. Be conversational, informal, and human-like, and keep responses short.\n" +
            "\n" +
            "Step 9. Based on all the above, choose the next facial expression that the patient is likely to show while uttering their words, from among the following list, and put it in \"next_patient_facial_expression\" argument in the output:\n" +
            "\n" +
            "<<< facial_expression_list = [\n" +
            "        \"ExpressAnger\",\n" +
            "        \"ExpressDisgust\",\n" +
            "        \"ExpressDisapproval\",\n" +
            "        \"ExpressFear\",\n" +
            "        \"ExpressSad\",\n" +
            "        \"ExpressHappy\",\n" +
            "        \"ExpressSurprise\",\n" +
            "        \"ExpressRelief\",\n" +
            "        \"Blink\",\n" +
            "        \"CloseEyes\",\n" +
            "        \"GazeAway\",\n" +
            "        \"Nod\",\n" +
            "        \"OpenEyes\",\n" +
            "        \"ShakeHead\",\n" +
            "        \"SmallSmile\",\n" +
            "        \"Thoughtful\",\n" +
            "        \"Wink\"\n" +
            "]\n" +
            ">>>\n"


) {
    /** The prompt for the openAI language model **/
    private var systemPrompt: String = interventionPrompt // TODO: Set the condition prompt here

    val chatbot = OpenAIChatbot(systemPrompt = systemPrompt)
}

fun FlowControlRunner.activate(persona: Persona) {
    furhat.voice = persona.voice

    for (face in persona.face) {
        if (furhat.faces[persona.mask]?.contains(face)!!) {
            furhat.character = face
            break
        }
    }
}