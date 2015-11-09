#!/Users/nicolas/.rbenv/shims/ruby

require 'csv'

## Util

def idize_country_name string
  string.downcase.strip.gsub(" ", "-").gsub(",", "")
end

## Sort CSVs by portal names 

# quote_chars = %w(" | ~ ^ & *)
# begin
#   inception = CSV.read('inception.csv', headers: true, quote_char: quote_chars.shift)
#   inception.sort_by! { |row| row[0]}
# rescue CSV::MalformedCSVError
#   quote_chars.empty? ? raise : retry
# end

## English version

fileHtml = File.new("inception.html", "w+")

CSV.foreach('./countries.csv') do |country|
fileHtml.puts "<h2 id='#{idize_country_name(country[0])}'>#{country[0]}</h2>"
  CSV.foreach('in.csv', headers: true, col_sep: ';', quote_char: "|") do |row|
    if country[0] == row[2]
      fileHtml.puts "<li><a href='#{row[4]}' target='_blank'>#{row[0]}</a></li>"
    end
  end
fileHtml.puts "<p style='text-align: right;'><span style='color: #17a2a2;'><a style='color: #17a2a2;' href='#top'>Report an issue</a></span><span> - </span><span style='color: #17a2a2;'><a style='color: #17a2a2;' href='#top'>Back to top of page</a></span></p>"
end

fileHtml.close()

## French version

fileHtml_fr = File.new("inception_fr.html", "w+")

CSV.foreach('./countries_fr.csv') do |country|

fileHtml_fr.puts "<h2 id='#{idize_country_name(country[0])}'>#{country[0]}</h2>"
  CSV.foreach('in.csv', headers: true, col_sep: ';', quote_char: "|") do |row|
    if country[0] == row[3]
      fileHtml_fr.puts "<li><a href='#{row[4]}' target='_blank'>#{row[0]}</a></li>"
    end
  end
fileHtml_fr.puts "<p style='text-align: right;'><span style='color: #17a2a2;'><a style='color: #17a2a2;' href='#top'>Signaler une erreur</a></span><span> - </span><span style='color: #17a2a2;'><a style='color: #17a2a2;' href='#top'>Haut de page</a></span></p>"
end

fileHtml_fr.close()
