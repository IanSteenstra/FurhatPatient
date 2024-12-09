package furhatos.app.openaichat.flow.chatbot

import furhatos.app.openaichat.flow.*
import furhatos.app.openaichat.flow.main.Idle
import furhatos.app.openaichat.setting.activate
import furhatos.app.openaichat.setting.conditionType
import furhatos.app.openaichat.setting.currentPersona
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

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
                currentPersona.chatbot.getResponse()
            } as AppraisalSchema

            // Play around with these, but I found these to be the best so far
            val strength = 5.0
            val duration = 2.0

            val gesture = when (val gestureName = response.next_patient_facial_expression) {
                "ExpressAnger" -> furhat.gesture(MyExpressAnger)
                "Blink" -> Gestures.Blink(strength = strength, duration = duration)
                "CloseEyes" -> Gestures.CloseEyes(strength = strength, duration = duration)
                "ExpressDisgust" -> furhat.gesture(MyExpressDisgust)
                "ExpressDisapproval" -> furhat.gesture(MyExpressDisapproval)
                "ExpressFear" -> furhat.gesture(MyExpressFear)
                "ExpressSad" -> furhat.gesture(MyExpressSad)
                "ExpressHappy" -> furhat.gesture(MyExpressHappy)
                "ExpressRelief" -> furhat.gesture(MyExpressRelief)
                "GazeAway" -> Gestures.GazeAway(strength = strength, duration = duration)
                "Nod" -> Gestures.Nod(strength = strength, duration = duration)
                "OpenEyes" -> Gestures.OpenEyes(strength = strength, duration = duration)
                "ShakeHead" -> Gestures.Shake(strength = strength, duration = duration)
                "SmallSmile" -> Gestures.Smile(strength = strength, duration = duration)
                "ExpressSurprise" -> furhat.gesture(MyExpressSurprise)
                "Thoughtful" -> Gestures.Thoughtful(strength = strength, duration = duration)
                "Wink" -> Gestures.Wink(strength = strength, duration = duration)
                else -> {
                    println("Unknown gesture: $gestureName")
                    null
                }
            }

            //TODO
            gesture?.let { furhat.gesture(it) }
            //furhat.gesture(response.nonverbal_behavior)
            furhat.say(response.next_patient_utterance)
        } else if (conditionType == "control") { // Idle/Control
            val response = call {
                currentPersona.chatbot.getResponse()
            } as String

            furhat.say(response)
        }
        else { // Idle/ test
            val response = call {
                currentPersona.chatbot.getResponse()
            } as String

            furhat.say(response)
        }
        reentry()
    }

    onNoResponse {
        reentry()
    }
}
