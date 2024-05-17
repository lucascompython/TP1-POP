Invoke-WebRequest -Uri "https://download.java.net/java/early_access/jextract/22/4/openjdk-22-jextract+4-30_windows-x64_bin.tar.gz" -OutFile "jextract.tar.gz"

tar -xvzf jextract.tar.gz --strip-components=1

Remove-Item -Path "jextract.tar.gz"