import scrapy
import datetime
import unicodedata

class CloudSpider(scrapy.Spider):
    name = "cloudspider"

    def start_requests(self):

        inputfilname='quotes-fineart_main_output.txt'

        with open(inputfilname) as f1:
            urls = f1.readlines()
        # you may also want to remove whitespace characters like `\n` at the end of each line
            urls = [x.strip() for x in urls]

            for url in urls:
                yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        #page = response.url.split("/")[-2]
        filename = 'data-fine-art.csv'
        with open(filename, 'ab') as f:


            painturl=response.url
            paintingname = response.selector.xpath("//div[@class='small-12 medium-6 large-12 columns art-meta']//h3//text()").extract()
            paintingimage = response.selector.xpath("//img[@itemprop='image']").xpath('@src')[0].extract()
            keywords=response.selector.xpath("//meta[@property='bt:keywords']").xpath('@content').extract()

            f.write(
                      str(("".join(str(e).replace("\n", "") for e in paintingname).replace(",", "").replace("[u'", "")).replace("']", ""))
                     +","
                     +str(painturl)
                     +","
                     +str(str(keywords).replace(",","|")).replace("[u'", "").replace("']", "")
                     +","
                     + "https:" + str(("".join(str(paintingimage))).replace("[u'", "")).replace("']", "")
                     +"\n")

        self.log('Saved file %s' % filename)
