package love.ytlsnb.quest.test;

public class LocationTest {
    // 地球平均半径，单位：米
    private static final double EARTH_RADIUS = 6371000;

    // 将角度转换为弧度
    private static double toRadians(double degree) {
        return degree * Math.PI / 180.0;
    }

    // 计算两个经纬度之间的距离，返回值单位为米
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = toRadians(lat1);
        double lat2Rad = toRadians(lat2);
        double deltaLat = toRadians(lat2 - lat1);
        double deltaLon = toRadians(lon2 - lon1);
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public static void main(String[] args) {
        double longitude1 = 119.513482;
        double latitude1 = 32.205501;
        double longitude2 = 119.513918;
        double latitude2 = 32.202689;
        System.out.println(calculateDistance(latitude1, longitude1, latitude2, longitude2));
    }
}
