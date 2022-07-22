# analytics
Minecraft (Bukkit) plugin designed for server administrators to track player traffic from hostnames.

### Download
If you don't want to build yourself, you can download a pre-compiled JAR from the [releases](https://github.com/RyloRS/analytics/releases) page.

### Building
Analytics uses [Maven](https://maven.org).
```bash
git clone https://github.com/RyloRS/analytics.git
cd analytics
mvn clean install
```

### Usage
This plugin has one command available.
* `/analytics`
  * `(hostname)` `[analytics.use]`- Returns the amount of unique users who have joined and total number of connections made through
 the specified hostname.

### License
[Apache](https://github.com/RyloRS/analytics/blob/master/LICENSE) © [RyloRS](https://github.com/RyloRS)