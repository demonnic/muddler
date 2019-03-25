function demonnicOnInstall(_, package)
    if package:find("YATCO") then
        demonnicOnStart()
    end
  end