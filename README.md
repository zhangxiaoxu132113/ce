#Crawl Enginer

ce这个项目负责了爬虫的角色，为uubook这个项目提供了数据。但同时这个项目会有多个定时任务，完成一些数据的分析，清洗。


**MW_CE爬虫规则的调整**
之前每爬取不同的网站就需要重新写一个Factory工厂类，在创建文章类IArticle的方法里面根据具体的css样式规则创建。
如果爬取的网站多了，创建很多个工厂类不说，还要每次针对不同的css样式写代码，这样处理起来很麻烦，项目也会很臃肿。
所以，我想通过配置文件的方式来实现。
CrawlBox是一个爬虫的盒子，里面存放了各个网站对应的内容爬取样式，通过加载json配置文件放到容器内。

**API实现方式**
通过new一个CrawlAction对象来启动一个爬取文章内容的任务

CrawlAction crawlAction = new CrawlAction("IBM","Article",link) {
                        @Override
                        public void action(JsonObject obj) {

                        }
                    };
crawlAction.work();

回调方法里面的JsonObject对象就是我们想要爬取的数据，这样实现的一个好处就是屏蔽了底层的细节。
只要告诉CrawlAction对象，要抓取IBM网站，Article内容模块的link链接，任务交给了Action处理，处理完了
就会返回一个JSON对象给我们。
