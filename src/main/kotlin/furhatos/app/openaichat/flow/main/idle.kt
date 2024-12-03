package furhatos.app.openaichat.flow.main

import furhatos.app.openaichat.flow.chatbot.MainChat
import furhatos.app.openaichat.setting.activate
import furhatos.app.openaichat.setting.currentPersona
import furhatos.flow.kotlin.*

val Idle : State = state {
    onEntry {
        activate(currentPersona)
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(MainChat)
    }
}





