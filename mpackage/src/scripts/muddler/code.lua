Muddler = Muddler or {
  path = getMudletHomeDir(),
  watch = true,
  preremove = false,
  postremove = false,
  preinstall = false,
  postinstall = false,
}

function Muddler:new(options)
  options = options or {}
  local me = table.deepcopy(options)
  setmetatable(me, self)
  self.__index = self
  me.path = me.path:gsub("\\", "/")
  if me.watch then me:start() end
  return me
end

function Muddler:start()
  self:stop()
  self.watch = true
  self.eventHandler = registerAnonymousEventHandler("sysPathChanged", function(_, path)
    if path == self.path .. "/.output" then
      self:reload()
    end
  end)
  addFileWatch(self.path .. "/.output")
end

local function jsonload(file)
  local f = assert(io.open(file, 'r'), 'File not found: '..file)
  local t = f:read('*a')
  f:close()
  return yajl.to_value(t)
end

function Muddler:stop()
  self.watch = false
  if self.eventHandler then
    killAnonymousEventHandler(self.eventHandler)
    self.eventHandler = nil
  end
  removeFileWatch(self.path .. "/.output")
end

local function execute(item)
  if not item then
    debugc("Attempted to execute nil or false, must be string or function")
  end
  local itype = type(item)
  if itype == "string" then
    local f,e = loadstring("return " .. item)
    if not f then
      f, e = loadstring(item)
    end
    if not f then
      debugc("Unable to convert string to function for execution in Muddler hook:" .. e)
      return
    end
    item = f
    itype = type(item)
  end
  if itype ~= "function" then
    debugc("Unable to execute item, need a function, got a " .. itype)
    return
  end
  local worked, err = pcall(item)
  if not worked then
    debugc("Error executing item: " .. tostring(err))
  end
end

function Muddler:reload()
  local ok, pkgInfo = pcall(jsonload, self.path .. "/.output")
  if not ok then
    debugc("Error loading output file, err: " .. pkgInfo)
    return
  end
  if type(pkgInfo) == "function" then
    return
  end
  local name = pkgInfo.name
  local path = self.path .. pkgInfo.path
  local prer, postr, prei, posti = self.preremove, self.postremove, self.preinstall, self.postinstall
  debugc("preremove " .. name)
  if prer then
    debugc(f"  Firing preremove for pkg: {name}")
    execute(prer)
    debugc(f"  END premove for pkg: {name}")
  end
  uninstallPackage(name)
  debugc("postremove " .. name)
  if postr then
    debugc(f"  Firing postremove for pkg: {name}")
    execute(postr)
    debugc(f"  END postmove for pkg: {name}")
  end
  debugc("preinstall " .. name)
  if prei then
    debugc(f"  Firing preinstall for pkg: {name}")
    execute(prei)
    debugc(f"  END preinstall for pkg: {name}")
  end
  local succ = installPackage(path)
  if not succ then
    debugc("Could not install package at " .. path)
    return
  end
  debugc("postinstall " .. name)
  if posti then
    debugc(f"  Firing postinstall for pkg: {name}")
    execute(posti)
    debugc(f"  END postinstall for pkg: {name}")
  end
  debugc("Done reloading pkg " .. name)
end
