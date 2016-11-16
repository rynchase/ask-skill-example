{
  "intents": [
    {
      "intent": "WhatsUpWithPersonIntent",
       "slots": [
        {
          "name": "firstName",
          "type": "AMAZON.US_FIRST_NAME"
        }
       ]
    },
    {
      "intent": "WhatsUpWithPersonTwoNameIntent",
       "slots": [
        {
          "name": "firstName",
          "type": "AMAZON.US_FIRST_NAME"
        },
        {
          "name": "lastName",
          "type": "LAST_NAME"
        }
       ]
    },
    {
       "intent": "IncompleteIntent"
    },
    {
       "intent": "CancelIntent"
    },
    {
      "intent": "AMAZON.HelpIntent"
    }
  ]
}
