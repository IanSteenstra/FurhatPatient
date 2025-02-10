package furhatos.app.openaichat.flow.chatbot

import furhatos.app.openaichat.flow.*
import furhatos.app.openaichat.flow.main.Idle
import furhatos.app.openaichat.setting.activate
//import furhatos.app.openaichat.setting.conditionType
import furhatos.app.openaichat.setting.currentPersona
import furhatos.flow.kotlin.*
import furhatos.gestures.Gesture
import furhatos.gestures.Gestures

// initialize Beliefs, Desires, Intentions - will get updated after each turn
var Current_BDI = "   {\n" +
        "            \"beliefs\": {\n" +
        "                \"belief1\": \"I don't have any issues with my habits.\",\n" +
        "                \"belief2\": \"I don't need anyone in the world to approve of my actions.\"\n" +
        "            },\n" +
        "            \"desires\": {\n" +
        "                \"desire1\": \"I wish I didn't have to come here. Just wanna get out.\"\n" +
        "            },\n" +
        "            \"intentions\": {\n" +
        "                \"intention1\": \"My health is not in bad shape. I'm still young.\"\n" +
        "            }\n" +
        "        }"


val conditionType = currentPersona.conditionType
var systemPrompt = currentPersona.systemPrompt

val MainChat = state(Parent) {
    onEntry {
        activate(currentPersona)
        delay(2000)
        Furhat.dialogHistory.clear()
        furhat.say("Hi, my name is ${currentPersona.name}")
        reentry()
    }

    onReentry {
        furhat.listen()
    }

    onResponse("can we stop", "goodbye") {
        furhat.say("Okay, goodbye")
        delay(2000)
        goto(Idle)
    }

    onResponse {
        furhat.gesture(GazeAversion(2.0))

        if (conditionType == "intervention") {
            val response = call {
                currentPersona.chatbot.getResponse(systemPrompt + "$Current_BDI***")
            } as AppraisalSchema

            // Play around with these, but I found these to be the best so far
            val strength = 5.0
            val duration = 2.0

            val gesture = when (val gestureName = response.next_patient_facial_expression) {
                "ExpressAnger" -> (MyExpressAnger)
                "Blink" -> (Gestures.Blink(strength = strength, duration = duration))
                "CloseEyes" -> (Gestures.CloseEyes(strength = strength, duration = duration))
                "ExpressDisgust" -> (MyExpressDisgust)
                "ExpressDisapproval" -> (MyExpressDisapproval)
                "ExpressFear" -> (MyExpressFear)
                "ExpressSad" -> (MyExpressSad)
                "ExpressHappy" -> (MyExpressHappy)
                "ExpressRelief" -> (MyExpressRelief)
                "GazeAway" -> (Gestures.GazeAway(strength = strength, duration = duration))
                "Nod" -> (Gestures.Nod(strength = strength, duration = duration))
                "OpenEyes" -> (Gestures.OpenEyes(strength = strength, duration = duration))
                "ShakeHead" -> (Gestures.Shake(strength = strength, duration = duration))
                "SmallSmile" -> (Gestures.Smile(strength = strength, duration = duration))
                "ExpressSurprise" -> (MyExpressSurprise)
                "Thoughtful" -> (Gestures.Thoughtful(strength = strength, duration = duration))
                "Wink" -> (Gestures.Wink(strength = strength, duration = duration))
                else -> {
                    println("Unknown gesture: $gestureName")
                    null
                }
            }

            Current_BDI = response.updated_bdi.toString()

            //TODO
            println("Evaluated gesture type: ${it::class}")

            gesture?.let { furhat.gesture(it as Gesture) }
            //furhat.gesture(response.nonverbal_behavior)
            furhat.say(response.next_patient_utterance)
        } else if (conditionType == "control") { // Idle/Control
            val response = call {
                currentPersona.chatbot.getResponse(systemPrompt)
            } as String

            furhat.say(response)
        }
        // if not control or intervention, nothing will be said --> good for gesture testing purposes
        else { // Idle/Control
            val response: String = ""
            furhat.say(response)
        }
        reentry()
    }

    onNoResponse {
        reentry()
    }
}
