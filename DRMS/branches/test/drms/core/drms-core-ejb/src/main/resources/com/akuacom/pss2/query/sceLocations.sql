SELECT loc.type AS locationType, loc.name AS locationName, loc.number AS locationNumber
FROM drwebsite.location loc
[WHERE loc.type=${locType}]
ORDER BY locationType,locationNumber