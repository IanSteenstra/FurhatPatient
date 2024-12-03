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

            val gesture = when (val gestureName = response.gesture) {
                "BigSmile" -> Gestures.BigSmile(strength = strength, duration = duration)
                "Blink" -> Gestures.Blink(strength = strength, duration = duration)
                "BrowFrown" -> Gestures.BrowFrown(strength = strength, duration = duration)
                "BrowRaise" -> Gestures.BrowRaise(strength = strength, duration = duration)
                "CloseEyes" -> Gestures.CloseEyes(strength = strength, duration = duration)
                "ExpressAnger" -> Gestures.ExpressAnger(strength = strength, duration = duration)
                "ExpressDisgust" -> Gestures.ExpressDisgust(strength = strength, duration = duration)
                "ExpressFear" -> Gestures.ExpressFear(strength = strength, duration = duration)
                "ExpressSad" -> Gestures.ExpressSad(strength = strength, duration = duration)
                "GazeAway" -> Gestures.GazeAway(strength = strength, duration = duration)
                "Nod" -> Gestures.Nod(strength = strength, duration = duration)
                "Oh" -> Gestures.Oh(strength = strength, duration = duration)
                "OpenEyes" -> Gestures.OpenEyes(strength = strength, duration = duration)
                "Roll" -> Gestures.Roll(strength = strength, duration = duration)
                "Shake" -> Gestures.Shake(strength = strength, duration = duration)
                "Smile" -> Gestures.Smile(strength = strength, duration = duration)
                "Surprise" -> Gestures.Surprise(strength = strength, duration = duration)
                "Thoughtful" -> Gestures.Thoughtful(strength = strength, duration = duration)
                "Wink" -> Gestures.Wink(strength = strength, duration = duration)
                else -> {
                    println("Unknown gesture: $gestureName")
                    null
                }
            }

            gesture?.let { furhat.gesture(it) }
            furhat.say(response.next_patient_utterance)
        } else { // Idle/Control
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
