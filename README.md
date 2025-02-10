# SimPatient: Emotionally Realistic Simulated Patients for Counselor Empathy Training

## Authors
Ian Steenstra*, Farnaz Nouraei*, Nishtha Sawhney*
*Northeastern University

## Abstract
This research explores using Large Language Models (LLMs) to create emotionally realistic simulated patients for counselor empathy training. We introduce SimPatient, a system embodied by the Furhat robot, designed to help novice counselors practice their skills. Two versions were tested: a control with basic dialogue and an intervention with appraisal-based emotion modeling and nonverbal behaviors (NVBs). A study with 6 participants compared perceived realism and impact on counselor empathy. While the emotion model enhanced perceived realism, particularly regarding NVBs, it did not increase empathy and, surprisingly, significantly decreased it in the intervention group. This suggests that adding emotional responses alone may not improve empathy and highlights the complexity of simulating realistic patient interactions, especially with resistant patients. Future research with larger samples and refined implementations is needed.

## Keywords
Large Language Models, Social Robots, Embodied Interaction, Appraisal Theory, Social Skills Training

## Introduction
Empathy is crucial for counselors. Traditional training methods like role-playing with standardized patients struggle to replicate the emotional complexity of real-world client interactions. This study introduces SimPatient, a system using LLMs and the Furhat robot to create emotionally realistic simulated patients, specifically focusing on resistant patients with alcohol misuse issues.

## Background - Appraisal Theory
Appraisal theory explains how emotions arise from an individual's evaluation of a situation based on dimensions like relevance, desirability, and controllability. This theory is used to model emotions in SimPatient.

## Related Work
This section discusses related work in:
- **Appraisal Theory & LLMs:** How appraisal theory is used to model emotions in LLMs.
- **Standardized Patients:** The traditional use of actors as patients and their limitations.
- **Patient Simulation Systems:** Existing systems using chatbots, VR, and other technologies.
- **LLMs for Simulated Patient Dialogue:** Recent work using LLMs for dialogue generation in patient simulations, including challenges like positivity bias.

## System Design
SimPatient uses GPT-4o for utterance generation and the Furhat robot for embodiment.
- **Two versions:** Control (basic dialogue) and Intervention (emotion module enabled).
- **Emotion Module (Intervention):** Uses appraisal theory to generate emotions and NVBs, incorporating attention, appraisal, PAD, emotion labels, coping, and Beliefs, Desires, and Intentions (BDI).
- **Code:** [https://github.com/IanSteenstra/FurhatPatient](https://github.com/IanSteenstra/FurhatPatient)

## Experiment
A between-subjects study (n=6) evaluated the impact of the emotion module.
- **Procedure:** Pre-interaction survey, 10-minute interaction with SimPatient ("Alex" persona), post-interaction survey, and semi-structured interview.
- **Measures:** Perth Empathy Scale (pre/post), Perceived Emotional Realism questions (post).

### Results
- **Empathy:** No significant change in the control group. Intervention group showed a significant *decrease* in total empathy.
- **Realism:** No significant difference between groups, but qualitative data suggests the emotion module increased perceived realism through NVBs.
- **Observations:** Intervention group had longer response times (2-5 seconds vs. <1 second in control). Participants generally found the simulated patient realistic and resistant.

## Discussion
- **RQ1 (Realism):** Emotion module likely influenced perceived realism, particularly through NVBs.
- **RQ2 (Empathy):** No increase, and a surprising decrease, in empathy in the intervention group. This may be due to patient resistance, short interaction time, or small sample size.
- **Limitations:** Small sample size, short interaction time, technical issues (ASR, facial tracking).
- **Future Work:** Larger and more diverse sample, longer interactions, improved technical implementation, exploration of specific mechanisms influencing counselor responses, and expanded evaluation metrics.

## Conclusion
This study explored using emotionally realistic simulated patients for counselor training. While the emotion model impacted perceived realism, it did not increase and even decreased empathy. Future research is needed to refine the system and fully explore its potential. This work contributes to the field of affective computing in healthcare and provides insights for developing more effective virtual patient training tools.

## Acknowledgements
Details contributions of each author (Coding, Conducting Study, Data Analysis, etc.)

## References
Includes references to relevant academic papers and resources.

## Notes: Running
- Add your OPENAI_API_KEY to the env variables in the MainKt run configuration.
- Add '-Dfurhatos.skills.brokeraddress=\<Your Furhat IP Address>' to the VM options in the MainKt run configuration.