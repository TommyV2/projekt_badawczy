(pwd() != @__DIR__) && cd(@__DIR__) # allow starting app from bin/ dir

using JuliaGenie
const UserApp = JuliaGenie
JuliaGenie.main()
