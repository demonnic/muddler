if demonnic.chat.config.blink then
    demonnic.chat.config.blink = false
    demonnic.chat.tabsToBlink = {}
    demonnic:echo("Blinking temporarily turned <red>off<grey>. It will reset if you edit your tabbed chat configuration, or close and reopen mudlet. To make it permanent, change demonnic.chat.config.blink to false in \"Demonnic->Tabbed Chat->Configuration options\" under scripts\n")
  else
    demonnic.chat.config.blink = true
    demonnic.chat:blink()
    demonnic:echo("Blinking temporarily turned <red>on<grey>. It will reset if you edit your tabbed chat configuration, or close and reopen mudlet. To make it permanent, change demonnic.chat.config.blink to true in \"Demonnic->Tabbed Chat->Configuration options\" under scripts\n")
  end