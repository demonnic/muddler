demonnic = demonnic or {}
demonnic.anitimer = demonnic.anitimer or {}
demonnic.anitimer.timers = demonnic.anitimer.timers or {}
demonnic.anitimer.activeTimers = demonnic.anitimer.activeTimers or {}

function demonnic.anitimer:new(name, cons, time, options)
  if options and type(options) ~= "table" then
    error("demonnic.anitimer:new() -- if you provide a fourth parameter, it must be a table. Please see http://github.com/demonnic/animatedtimers for detailed usage information")
  end
  if not options then
    options = {}
  end
  if options.showTime == nil then options.showTime = true end 
  options.timerCaption = options.timerCaption or ""

  if not demonnic.anitimer.timers[name] then
    demonnic.anitimer.timers[name] = {}
    demonnic.anitimer.timers[name].gauge = Geyser.Gauge:new(cons, options.container)
    demonnic.anitimer.timers[name].watch = createStopWatch()
  else
    for k,v in pairs(cons) do
      demonnic.anitimer.timers[name].gauge[k] = v
    end
    if options.container then
      options.container:add(demonnic.anitimer.timers[name].gauge)
    else
      Geyser:add(demonnic.anitimer.timers[name].gauge)
    end
  end
    demonnic.anitimer.timers[name].max = time
    demonnic.anitimer.timers[name].current = time
    demonnic.anitimer.timers[name].showTime = options.showTime
    demonnic.anitimer.timers[name].timerCaption = options.timerCaption
		demonnic.anitimer.timers[name].hook = options.hook
    if options.cssFront then
      if not options.cssBack then
        options.cssBack = options.cssFront .. "background-color: black;"
      end
      demonnic.anitimer.timers[name].gauge:setStyleSheet(options.cssFront, options.cssBack)
    end
    if not table.contains(demonnic.anitimer.activeTimers, name) then table.insert(demonnic.anitimer.activeTimers, name) end
    resetStopWatch(demonnic.anitimer.timers[name].watch)
    startStopWatch(demonnic.anitimer.timers[name].watch)
    demonnic.anitimer:update(name)
end

function demonnic.anitimer:getTime(name)
  if not demonnic.anitimer.timers[name] then return 0 end
  local max = demonnic.anitimer.timers[name].max
  local current = getStopWatchTime(demonnic.anitimer.timers[name].watch)
  local newValue = max - current
  return newValue
end

function demonnic.anitimer:showTimers()
  demonnic:echo("<yellow>List of animated timers which have been created")
  for timer,_ in pairs(demonnic.anitimer.timers) do
    demonnic:echo(string.format("   %s", timer))
  end
  local actives = ""
  for _,timer in ipairs(demonnic.anitimer.activeTimers) do
    if actives == "" then
      actives = timer
    else
      actives = string.format("%s, %s", actives, timer)
    end
  end
  demonnic:echo("<yellow>Active timers: <red>" .. actives)
end

function demonnic.anitimer:update(name)
  if not demonnic.anitimer.timers[name] then return nil end
  demonnic.anitimer.timers[name].current = demonnic.anitimer:getTime(name)
  local time = demonnic.anitimer.timers[name].current
  demonnic.anitimer.timers[name].text = (demonnic.anitimer.timers[name].showTime and string.format("%.1f %s", time, demonnic.anitimer.timers[name].timerCaption or "")) or (demonnic.anitimer.timers[name].timerCaption or "")
  demonnic.anitimer.timers[name].gauge:setValue(demonnic.anitimer.timers[name].current, demonnic.anitimer.timers[name].max, demonnic.anitimer.timers[name].text)
end

function demonnic.anitimer:stop(name)
  if not demonnic.anitimer.timers[name] then return nil end
  stopStopWatch(demonnic.anitimer.timers[name].watch)
  demonnic.anitimer.timers[name].gauge:hide()
  for i,v in pairs(demonnic.anitimer.activeTimers) do
    if demonnic.anitimer.timers[v] == demonnic.anitimer.timers[name] then
      table.remove(demonnic.anitimer.activeTimers, i)
      return
    end
  end
end

function demonnic.anitimer:stopAll()
  for _,name in pairs(demonnic.anitimer.activeTimers) do
    demonnic.anitimer:stop(name)
  end
end

function demonnic.anitimer:pause(name)
  if not demonnic.anitimer.timers[name] then return nil end
  stopStopWatch(demonnic.anitimer.timers[name].watch)
  for i,v in pairs(demonnic.anitimer.activeTimers) do
    if v == name then
      table.remove(demonnic.anitimer.activeTimers, i)
      return
    end
  end
end

function demonnic.anitimer:pauseAll()
  for name,_ in pairs(demonnic.anitimer.timers) do
    demonnic.anitimer:pause(name)
  end
end

function demonnic.anitimer:destroy(name)
  if not demonnic.anitimer.timers[name] then return nil end
  demonnic.anitimer:stop(name)
  demonnic.anitimer.timers[name] = nil
  return true
end

function demonnic.anitimer:destroyAll()
  for name,_ in pairs(demonnic.anitimer.timers) do
    demonnic.anitimer:destroy(name)
  end
end

function demonnic.anitimer:start(name)
  if not demonnic.anitimer.timers[name] then return nil end
  local current = demonnic.anitimer.timers[name].current
  if current == 0 then return nil end
  demonnic.anitimer.timers[name].max = current
  resetStopWatch(demonnic.anitimer.timers[name].watch)
  startStopWatch(demonnic.anitimer.timers[name].watch)
  if not table.contains(demonnic.anitimer.activeTimers, name) then table.insert(demonnic.anitimer.activeTimers, name) end
  demonnic.anitimer.timers[name].gauge:show()
  demonnic.anitimer:update(name)
end

function demonnic.anitimer:startAll()
  for name,_ in pairs(demonnic.anitimer.timers) do
    if not table.contains(demonnic.anitimer.activeTimers, name) then
      demonnic.anitimer:start(name)
    end
  end
end

function demonnic.anitimer:animate()
  for i,v in pairs(demonnic.anitimer.activeTimers) do
    name = v
    demonnic.anitimer:update(name)
    if demonnic.anitimer:getTime(name) <= 0 then
      demonnic.anitimer.timers[name].gauge:hide()
      table.remove(demonnic.anitimer.activeTimers, i)
      stopStopWatch(demonnic.anitimer.timers[name].watch)
      demonnic.anitimer.timers[name].max = 0
      demonnic.anitimer.timers[name].current = 0
			demonnic.anitimer:executeHook(name)
    end
  end
end

function demonnic.anitimer:executeHook(name)
  local hook = demonnic.anitimer.timers[name].hook
	local r = ""
	if hook == nil then
	  return
	elseif type(hook) == "string" then
	  local f,e = loadstring("return " .. hook)
		if not f then
		  f,e = assert(loadstring(hook))
		end
		f()
	elseif type(hook) == "function" then
	  hook()
	else
	  error("AnimatedTimers: you've provided a hook for " .. name .. " which is neither a string nor a function. Unable to execute")
	end
end