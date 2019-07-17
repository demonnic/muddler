demonnic.newContainer = demonnic.newContainer or Geyser.Container:new({x=0, y=0, height=400, width = 200})
local myCss1 = [[
border-width: 4px;
border-radius: 7;
border-color: red;
background-color: green;
]]
local myCss2 = [[
border-width: 4px;
border-radius: 7;
border-color: green;
background-color: red;
]]
demonnic.anitimer:new("Test1", {x=200, y=100, height = 30, width = 300, color="red"}, 5)
demonnic.anitimer:new("Test2", {x = 0, y="50%", height = 20, width = "100%"}, 10, {container = demonnic.newContainer, showTime = true, timerCaption = "Test2", cssFront = myCss1, cssBack = myCss2})
demonnic.anitimer:new("Test3", {x = 500, y = 300, height = 40, width = 100}, 8, {showTimer = false, timerCaption = "Test3", cssFront = myCss2, cssBack = myCss1})
demonnic.anitimer:new("Test4", {x=-20, y=0, height = 20, width = "100%"}, 18, {container = demonnic.newContainer, showtime = false})