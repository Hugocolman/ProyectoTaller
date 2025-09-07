Param(
  [Parameter(Mandatory=$false)] [string]$WhatIf = "true"
)

$srcTemplates = "src/main/resources/templates"
$srcStatic    = "src/main/resources/static"
$binTemplates = "target/classes/templates"
$binStatic    = "target/classes/static"

function Sync($from, $to) {
  if (-not (Test-Path $from)) { Write-Host "No existe: $from"; return }
  if (-not (Test-Path $to))   { New-Item -ItemType Directory -Force -Path $to | Out-Null }
  $opts = @("/E", "/NJH", "/NJS", "/NP")
  if ($WhatIf -eq "true") { $opts += "/L" }
  & robocopy $from $to $opts | Out-Null
}

Write-Host "Sincronizando templates... (WhatIf=$WhatIf)"
Sync $binTemplates $srcTemplates
Write-Host "Sincronizando static... (WhatIf=$WhatIf)"
Sync $binStatic $srcStatic

Write-Host "Listo. Si todo se ve bien, ejecutar con -WhatIf false para copiar."

