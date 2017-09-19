import scrapy
import datetime
import unicodedata

class PaintingsSpider(scrapy.Spider):
    name = "painter"

    def start_requests(self):

        inputfilname='newauthor.txt'

        with open(inputfilname) as f1:
            urls = f1.readlines()
        # you may also want to remove whitespace characters like `\n` at the end of each line
            urls = [x.strip() for x in urls]

            for url in urls:
                yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        #page = response.url.split("/")[-2]
        filename = 'Authdetails_new.csv'
        with open(filename, 'ab') as f:
            #Authorurl=response.selector.xpath("//meta[@property='bt:url']").xpath('@content').extract()
            Authorurl=response.url
            Following = response.selector.xpath("//div[@class='about-follow-nav']//a//text()")[1].extract()

            followers = response.selector.xpath("//div[@class='about-follow-nav']//a//text()")[4].extract()

            Artworks = response.selector.xpath("//a[@title='Link to Artworks']//span//text()").extract()

            Collections = response.selector.xpath("//a[@title='Link to Collections']//span//text()").extract()

            Favorititesauthorlevel = response.selector.xpath("//a[@title='Link to Favorites']//span//text()").extract()

            education = response.selector.xpath("//div[@class='profile-about small-12 medium-6 large-4 columns']//section[@itemprop='description']//p//text()")[1].extract()

            exhibitions= response.selector.xpath("//div[@class='profile-about small-12 medium-6 large-4 columns']//section[@class='events']//p//text()").extract()

            f.write(str("".join(Following).replace("\n","").replace(",","")).replace("\n","").replace(",","").strip()
                    +","
                    +str("".join(followers).replace("\n","").replace(",","")).replace("\n","").replace(",","").strip()
                    +","
                    +str("".join(Artworks).replace("\n","").replace(",","")).replace("\n","").replace(",","").strip()
                    +","
                    +str("".join(Collections).replace("\n","").replace(",","")).replace("\n","").replace(",","").strip()
                    +","
                    +str("".join(Favorititesauthorlevel).replace("\n","").replace(",","")).replace("\n","").replace(",","").strip()
                  #  +","
                 #   +str("".join(education).replace("\n","").replace(",","")).replace("\n","").replace("\r","").replace(",","").strip()
                    +","
                    +str("".join(exhibitions).replace("\n","").replace(",","|")).replace("\r","").replace(",","|").strip()
                    +","
                    +str("".join(Authorurl).replace("\n","").replace(",","")).replace(",","").strip()
                    +"\n")

        self.log('Saved file %s' % filename)
