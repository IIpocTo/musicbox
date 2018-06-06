Script for importing data from Spotify Web API

## Prerequisites

* Python 2.7

## Install dependencies

```
python setup.py install
```

## Run script

```
python spotify_importer --client <client_id> --secret <secret_id>
```

If you want to rebuild the base rather than update add optional keyword

```
python spotify_importer --client <client_id> --secret <secret_id> --rebuild
```