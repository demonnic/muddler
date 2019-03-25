--Do not remove the following lines. Or change them.
demonnic = demonnic or {}
demonnic.chat = demonnic.chat or {}
demonnic.chat.tabsToBlink = demonnic.chat.tabsToBlink or {}
demonnic.chat.tabs = demonnic.chat.tabs or {}
demonnic.chat.windows = demonnic.chat.windows or {}
if not demonnic.chat.config then
  cecho("<red:white>YOU DO NOT HAVE THE YATCO CONFIG PACKAGE IN PLACE. THINGS WILL NOT WORK AS EXPECTED\n\n")
  demonnic.chat.error = "NO CONFIG"
end