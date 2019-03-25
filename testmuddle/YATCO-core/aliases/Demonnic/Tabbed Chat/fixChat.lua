local currentsetting = demonnic.chat.config.location
local newsetting = ""
if currentsetting == "topright" then 
  newsetting = "bottomleft" 
elseif currentsetting == "topleft" then
  newsetting = "bottomright"
elseif currentsetting == "bottomleft" then
  newsetting = "topright"
elseif currentsetting == "bottomright" then
  newsetting = "topleft"
end

demonnic.chat.config.location = newsetting
demonnic.chat:create()
demonnic.chat.config.location = currentsetting
demonnic.chat:create()