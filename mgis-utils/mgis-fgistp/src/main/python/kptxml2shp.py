import xml.etree.ElementTree as ET
import shapefile
import os

xml_file = '../../test/resources/ru/sovzond/mgis2/rusregister/kpt/doc22169826.xml'
shape_file = 'output.shp'
projection = 'GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984",SPHEROID["WGS_1984",6378137.0,298.257223563]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]]'

w = shapefile.Writer(shapefile.POLYGON)

# create fields
w.field("CadastralNumber")
w.field("State")
w.field("DateCreated")
w.field("AreaArea")
w.field("AreaUnit")
w.field("Name")
w.field("LocationInBounds")
w.field("LocationAddressOKATO")
w.field("LocationAddressKLADR")
w.field("LocationAddressRegion")
w.field("LocationAddressDistrictName")
w.field("LocationAddressDistrictType")
w.field("LocationAddressCityName")
w.field("LocationAddressCityType")
w.field("LocationAddressLocalityName")
w.field("LocationAddressLocalityType")
w.field("LocationAddressStreetName")
w.field("LocationAddressStreetType")
w.field("LocationAddressLevel1Type")
w.field("LocationAddressLevel1Value")
w.field("LocationAddressNode")
w.field("Category")
w.field("UtilizationUtilization")
w.field("UtilizationByDoc")
w.field("CadastralCostValue")
w.field("CadastralCostUnit")

tree = ET.parse(xml_file)
root = tree.getroot()

for kpt in root.iter('KPT'):
    print(kpt)
    break

for kpt in root.iter('kpt'):
    print("asd")
    for blocks in kpt.iter("CadastralBlocks"):
        for block in blocks.iter("CadastralBlock"):
            for parcels in block.iter("Parcels"):
                for parcel in parcels.iter("Parcel"):
                    cadastralNumber = parcel.attributes["CadastralNumber"]
                    state = parcel.attributes["State"]
                    dateCreated = parcel.attributes["DateCreated"]
                    areaArea = ""
                    areaUnit = ""
                    okato = ""
                    kladr = ""
                    region = ""
                    districtName = ""
                    districtType = ""
                    cityName = ""
                    cityType = ""
                    localityName = ""
                    localityType = ""
                    streetName = ""
                    streetType = ""
                    level1Type = ""
                    level1Value = ""
                    for area in parcel.iter("Area"):
                        areaArea = area.get("Area")
                        areaUnit = area.get("Unit")
                    for location in parcel.iter("Location"):
                        for address in location.iter("Address"):
                            okato = address.get("ns2:OKATO")
                            kladr = address.get("ns2:KLADR")
                            region = address.get("ns2:Region")
                            districtName = address.get("ns2:Region").attributes["Name"]
                            districtType = address.get("ns2:Region").attributes["Type"]
                            cityName = address.get("ns2:Region").attributes["Name"]
                            cityType = address.get("ns2:Region").attributes["Type"]
                            localityName = address.get("ns2:Region").attributes["Name"]
                            localityType = address.get("ns2:Region").attributes["Type"]
                            streetName = address.get("ns2:Region").attributes["Name"]
                            streetType = address.get("ns2:Region").attributes["Type"]
                            level1Type = address.get("ns2:Region").attributes["Type"]
                            level1Value = address.get("ns2:Level1").attributes["Value"]
                    category = parcel.get("Category")
                    utilizationUtilization = parcel.get("Utilization").attributes["Utilization"]
                    utilizationByDoc = parcel.get("Utilization").attributes["ByDoc"]
                    cadastralCostValue = parcel.get("CadastralCost").attributes["Value"]
                    cadastralCostUnit = parcel.get("CadastralCost").attributes["Unit"]
                    entSys = parcel.get("EntitySpatial").attributes["EntSys"]
                    for entitySpatial in parcel.iter("EntitySpatial"):
                        part = []
                        for spatialElement in parcel.iter("ns3:SpelementUnit"):
                            for spelementUnit in parcel.iter("ns3:SpatialElement"):
                                for ordinate in parcel.iter("ns3:Ordinate"):
                                    # specify coordinates in X,Y order (longitude, latitude)
                                    ordNmb = location.attributes['OrdNmb']
                                    deltaGeopoint = location.attributes["DeltaGeopoint"]
                                    part.append([float(location.attributes['X']), float(location.attributes['Y'])])
                        w.poly(parts=[part])
                        # copy attributes

                    w.record(cadastralNumber, state, dateCreated, areaArea, areaUnit, okato, kladr, region, districtName, districtType, cityName,
                             cityType, localityName, localityType, streetName, streetType, level1Type, level1Value)
                    w.save(shape_file)  # create the PRJ file

with open(os.path.splitext(shape_file)[0] + os.extsep + 'prj', 'w') as prj:
    prj.write(projection)
