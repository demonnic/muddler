
function demonnic.chat:cecho(chat, message)
    local alltab = demonnic.chat.config.Alltab
    local blink = demonnic.chat.config.blink
    cecho(string.format("win%s",chat), message)
    if alltab and chat ~= alltab then 
      cecho(string.format("win%s", alltab), message)
    end
    if blink and chat ~= demonnic.chat.currentTab then
      if (alltab == demonnic.chat.currentTab) and not demonnic.chat.config.blinkOnAll then
        return
      else
        demonnic.chat.tabsToBlink[chat] = true
      end
    end
  end
  
  function demonnic.chat:decho(chat, message)
    local alltab = demonnic.chat.config.Alltab
    local blink = demonnic.chat.config.blink
    decho(string.format("win%s",chat), message)
    if alltab and chat ~= alltab then 
      decho(string.format("win%s", alltab), message)
    end
    if blink and chat ~= demonnic.chat.currentTab then
      if (alltab == demonnic.chat.currentTab) and not demonnic.chat.config.blinkOnAll then
        return
      else
        demonnic.chat.tabsToBlink[chat] = true
      end
    end
  end
  
  function demonnic.chat:hecho(chat, message)
    local alltab = demonnic.chat.config.Alltab
    local blink = demonnic.chat.config.blink
    hecho(string.format("win%s",chat), message)
    if alltab and chat ~= alltab then 
      hecho(string.format("win%s", alltab), message)
    end
    if blink and chat ~= demonnic.chat.currentTab then
      if (alltab == demonnic.chat.currentTab) and not demonnic.chat.config.blinkOnAll then
        return
      else
        demonnic.chat.tabsToBlink[chat] = true
      end
    end
  end
  
  function demonnic.chat:echo(chat, message)
    local alltab = demonnic.chat.config.Alltab
    local blink = demonnic.chat.config.blink
    echo(string.format("win%s",chat), message)
    if alltab and chat ~= alltab then 
      echo(string.format("win%s", alltab), message)
    end
    if blink and chat ~= demonnic.chat.currentTab then
      if (alltab == demonnic.chat.currentTab) and not demonnic.chat.config.blinkOnAll then
        return
      else
        demonnic.chat.tabsToBlink[chat] = true
      end
    end
  end