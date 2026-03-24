# ─── Load .env dan jalankan aplikasi ───────────────────────────────────────
$envFile = ".\.env"

if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        $line = $_.Trim()
        if ($line -and !$line.StartsWith("#") -and $line -match "=") {
            $idx   = $line.IndexOf("=")
            $key   = $line.Substring(0, $idx).Trim()
            $value = $line.Substring($idx + 1).Trim()
            [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
            Write-Host ">>> Set: $key=$value"
        }
    }
    Write-Host ">>> .env loaded sukses`n"
} else {
    Write-Host ">>> ERROR: file .env tidak ditemukan di $(Get-Location)"
    exit 1
}

./gradlew run
# ────────────────────────────────────────────────────────────────────────────