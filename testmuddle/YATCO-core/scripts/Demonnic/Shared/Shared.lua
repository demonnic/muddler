--Bootstrapping variables/etc. Don't touch this unless you really know what you're doing

--I mean it. I'll point. AND laugh. loudly. 
demonnic = demonnic or {}
demonnic.config = demonnic.config or {}
demonnic.balances = demonnic.balances or {}
demonnic.balances.balance = demonnic.balances.balance or 1
demonnic.balances.equilibrium = demonnic.balances.equilibrium or 1
demonnic.debug = demonnic.debug or {}
demonnic.debug.active = demonnic.debug.active or nil
demonnic.debug.categories = demonnic.debug.categories or { }


function demonnic:echo(msg)
 cecho(string.format("\n<blue>(<green>Demonnic<blue>):<white> %s", msg))
end