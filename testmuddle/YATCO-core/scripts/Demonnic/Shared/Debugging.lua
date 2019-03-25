--Adds debugging functionality 

function demonnic:Debug(category,debugData)
    if category then
       if table.contains(demonnic.debug.categories, category) then
          if type(debugData) == "table" then
             demonnic:echo("<red>DEBUG " .. category .. ":<white>")
             display(debugData)
          elseif type(debugData) == "string" or type(debugData) == "number" then
             demonnic:echo("<red>DEBUG " .. category .. ":<white> " .. debugData .. "\n" )
          else
             demonnic:echo("<red>DEBUG " .. category .. ":<white> " .. tostring(debugData) .. "\n" )
          end
       end
    else
       if type(debugData) == "table" then
          demonnic:echo("<red>DEBUG:<white>")
          display(debugData)
       elseif type(debugData) == "string" or type(debugData) == "number" then
          demonnic:echo("<red>DEBUG:<white> " .. debugData)
       else
          demonnic:echo("<red>DEBUG:<white> " .. tostring(debugData))
       end
    end
 end
 
 function demonnic:printDebug(category, debugData)
    if not demonnic.debug.active then return end
    demonnic:Debug(category, debugData)
 end
 
 function demonnic:toggleDebug()
    if demonnic.debug.active then demonnic.debug.active = nil
    else demonnic.debug.active = true
    end
    demonnic:echo("Debugging is currently " .. (( demonnic.debug.active and "<green>ON<white>") or "<red>OFF<white>"))
 end
 
 function demonnic:watchCategory( category )
    if table.contains(demonnic.debug.categories, category) then
       for i,v in ipairs(demonnic.debug.categories) do
          if v == category then
             table.remove(demonnic.debug.categories, i)
          end
       end
       demonnic:echo("No longer watching the '<red>"..category.."<white>' category.") 
    else
       table.insert(demonnic.debug.categories, category)
       demonnic:echo("Now watching the '<red>"..category.."<white>' category.")
    end
    demonnic:echo("Debugging is currently " .. (( demonnic.debug.active and "<green>ON<white>") or "<red>OFF<white>"))
 end
 
 function demonnic:listCategories()
    if #demonnic.debug.categories > 0 then
       demonnic:echo("You are currently watching the following categories:\n" .. table.concat(demonnic.debug.categories,", ") )
    else
       demonnic:echo("You are not watching any debugs.")
    end
 end
 