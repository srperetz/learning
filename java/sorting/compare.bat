@if "%2"=="" (
  @echo Usage: %0 A B
  @echo   where A and B are the two types of sorts to compare,
  @echo         any two of:  B[ubble], S[election], I[nsertion], M[erge], H[eap] or Q[uick]
  @goto DONE
)
@for %%I in (testfiles\*.txt) DO @diff -q output\%1.%%~nxI output\%2.%%~nxI
:DONE
