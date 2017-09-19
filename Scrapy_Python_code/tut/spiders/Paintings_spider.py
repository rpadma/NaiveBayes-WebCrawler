import scrapy
import datetime
import unicodedata

class PaintingsSpider(scrapy.Spider):
    name = "painting"

    def start_requests(self):

        inputfilname='painturls.txt'

        with open(inputfilname) as f1:
            urls = f1.readlines()
        # you may also want to remove whitespace characters like `\n` at the end of each line
            urls = [x.strip() for x in urls]

            for url in urls:
                yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        #page = response.url.split("/")[-2]
        filename = 'quotes.csv'
        with open(filename, 'ab') as f:

            subject = response.selector.xpath("//meta[@property='bt:subject']").xpath('@content').extract()

            paintingname = response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//h3//text()").extract()
            authorname   = response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//p//a//text()").extract()
            authorcountry =response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//p//span[@itemprop='nationality']//text()").extract()
            sizeheight=response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//p[@class='category-size']//span//text()")[0].extract()
            sizeWidth = response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//p[@class='category-size']//span//text()")[1].extract()
            sizedepth = response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//p[@class='category-size']//span//text()")[2].extract()
            price = response.selector.xpath("//div[@class='art-detail-ribbon']//div[@class='clearfix']//span[@class='price']//text()")[1].extract()
            views = response.selector.xpath("//div[@class='art-detail-stats']//ul//li//text()")[0].extract()
            favorites = response.selector.xpath("//div[@class='art-detail-stats']//ul//li//text()")[4].extract()
            publisheddate = response.selector.xpath("//meta[@property='bt:pubDate']").xpath('@content').extract()
            modifieddate = response.selector.xpath("//meta[@property='bt:modDate']").xpath('@content').extract()
            paintingtype = response.selector.xpath("//div[@class='small-12 columns']//p//span//text()").extract()
            #paintingtype = response.selector.xpath("//div[@class='small-12 columns']//p").extract_first()
            #  keyword = response.selector.xpath("//div[@class='small-12 columns']//p//text()")[8].extract()
            paintingimage = response.selector.xpath("//img[@itemprop='image']").xpath('@src')[0].extract()
            Authorurl=response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//p//a").xpath('@href')[0].extract()
            paintingurl= response.selector.xpath("//meta[@property='og:url']").xpath('@content').extract()
            f.write((str("".join(str(e).replace("\n","") for e in paintingname).replace("[u'","")).replace("']","").replace("'","")).replace(",","")
                     +","
                     +str(("".join(str(e).replace("\n","") for e in authorname).replace(",","").replace("[u'","")).replace("']",""))
                    # +str(("".join(str(unicodedata.normalize('NFKD',e).encode('ascii', 'ignore')) for e in authorname).replace(",","").replace("[u'","")).replace("']",""))
                     +","
                     +(str((" ".join(str(authorcountry)))).replace("[ u '","")).replace("' ]","")
                     +","
                     +str(sizeheight).replace("H","").strip()
                     +","
                     +str(sizeWidth).replace("W","").strip()
                     +","
                     +str(sizedepth).strip()
                     +","
                     +str((((price).strip()).replace(",","")).replace("\n","")).replace("$","")
                     +","
                     +str(views)
                     +","
                     +str(favorites)
                     +","
                     +str(("".join(str(publisheddate))).replace("[u'","")).replace("']","").strip()
                     +","
                     +str("".join(str(modifieddate)).replace("[u'","")).replace("']","")
                     +","
                     +(str(" ".join(str(e).replace("\n","") for e in paintingtype)).replace("\n","")).replace(",","").strip()
                     +","
                     +"https:"+str(("".join(str(paintingimage))).replace("[u'","")).replace("']","")
                     +","
                     +str("https://www.saatchiart.com"+str("".join(str(Authorurl))))
                     +","
                     +str("".join(str(paintingurl))).replace("[u'","").replace("']","")
                     +","
                     + str("".join(str(subject).replace("[u'", "")).replace("']",""))
                     + ","
                     +str(datetime.datetime.fromtimestamp(int((str(("".join(str(publisheddate))).replace("[u'","")).replace("']","").strip()))/1000.0).strftime('%Y-%m-%d'))
                     +","
                     +str(datetime.datetime.fromtimestamp(int((str(("".join(str(publisheddate))).replace("[u'","")).replace("']","").strip()))/1000.0).strftime('%Y-%m-%d'))
                     +"\n")

        self.log('Saved file %s' % filename)
