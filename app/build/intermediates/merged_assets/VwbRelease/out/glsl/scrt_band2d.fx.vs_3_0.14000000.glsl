uniform highp mat4 matProj;
uniform highp vec4 screenSize;
uniform highp mat4 matWorldView;
uniform highp mat4 matWorldViewProj;
uniform highp mat4 matDataTransform;
uniform highp mat4 matTexCoordTransform;
uniform highp vec4 PaletteSize;
uniform highp vec4 PenThicknessA;
uniform highp vec4 PenThicknessB;
uniform sampler2D PaletteTexture;
attribute highp vec3 vTexCoord1;
attribute highp vec2 vTexCoord2;
attribute highp vec3 vTexCoord3;
attribute highp vec4 vTexCoord5;
attribute highp vec4 vTexCoord6;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
void main ()
{
  highp mat4 vMatWorldViewProj_1;
  highp vec4 tmpvar_2;
  vMatWorldViewProj_1 = (matWorldView * matProj);
  highp vec3 tmpvar_3;
  highp vec3 tmpvar_4;
  highp vec3 tmpvar_5;
  highp vec2 tmpvar_6;
  highp vec3 tmpvar_7;
  highp vec3 tmpvar_8;
  highp vec4 tmpvar_9;
  highp vec4 tmpvar_10;
  highp vec3 tmpvar_11;
  tmpvar_11.x = matDataTransform[0].x;
  tmpvar_11.y = matDataTransform[1].y;
  tmpvar_11.z = matDataTransform[1].y;
  highp vec3 tmpvar_12;
  tmpvar_12.x = matDataTransform[0].w;
  tmpvar_12.y = matDataTransform[1].w;
  tmpvar_12.z = matDataTransform[1].w;
  tmpvar_3 = ((vTexCoord1 * tmpvar_11) + tmpvar_12);
  highp vec3 tmpvar_13;
  tmpvar_13.x = matDataTransform[0].x;
  tmpvar_13.y = matDataTransform[1].y;
  tmpvar_13.z = matDataTransform[1].y;
  highp vec3 tmpvar_14;
  tmpvar_14.x = matDataTransform[0].w;
  tmpvar_14.y = matDataTransform[1].w;
  tmpvar_14.z = matDataTransform[1].w;
  tmpvar_4 = ((vTexCoord3 * tmpvar_13) + tmpvar_14);
  if ((vTexCoord6.x == -1.0)) {
    tmpvar_4.yz = tmpvar_3.yz;
    highp vec2 tmpvar_15;
    highp float fIntersectionAmount_16;
    highp vec2 vIntersection_17;
    highp float tmpvar_18;
    tmpvar_18 = (tmpvar_3.z - tmpvar_3.y);
    highp float tmpvar_19;
    tmpvar_19 = (tmpvar_3.z - tmpvar_3.y);
    highp float tmpvar_20;
    tmpvar_20 = (tmpvar_18 * tmpvar_19);
    vIntersection_17 = vec2(0.0, 0.0);
    fIntersectionAmount_16 = 0.0;
    if ((tmpvar_20 < 0.0)) {
      highp vec3 tmpvar_21;
      highp float tmpvar_22;
      tmpvar_22 = (tmpvar_3.y - tmpvar_3.y);
      highp float tmpvar_23;
      tmpvar_23 = (tmpvar_3.x - tmpvar_4.x);
      highp float tmpvar_24;
      tmpvar_24 = ((tmpvar_22 * tmpvar_3.x) + (tmpvar_23 * tmpvar_3.y));
      highp float tmpvar_25;
      tmpvar_25 = (tmpvar_3.z - tmpvar_3.z);
      highp float tmpvar_26;
      tmpvar_26 = (tmpvar_3.x - tmpvar_4.x);
      highp float tmpvar_27;
      tmpvar_27 = ((tmpvar_25 * tmpvar_3.x) + (tmpvar_26 * tmpvar_3.z));
      highp float tmpvar_28;
      tmpvar_28 = ((tmpvar_22 * tmpvar_26) - (tmpvar_25 * tmpvar_23));
      if ((tmpvar_28 == 0.0)) {
        tmpvar_21 = vec3(3.402823e+38, 3.402823e+38, 0.0);
      } else {
        highp vec3 tmpvar_29;
        tmpvar_29.z = 1.0;
        tmpvar_29.x = (((tmpvar_26 * tmpvar_24) - (tmpvar_23 * tmpvar_27)) / tmpvar_28);
        tmpvar_29.y = (((tmpvar_22 * tmpvar_27) - (tmpvar_25 * tmpvar_24)) / tmpvar_28);
        tmpvar_21 = tmpvar_29;
      };
      vIntersection_17 = tmpvar_21.xy;
      fIntersectionAmount_16 = tmpvar_21.z;
    };
    fIntersectionAmount_16 = (fIntersectionAmount_16 * vTexCoord5.w);
    tmpvar_15.y = 0.0;
    highp vec3 tmpvar_30;
    tmpvar_30.z = 0.0;
    tmpvar_30.xy = mix (mix (tmpvar_3.xy, tmpvar_3.xz, vTexCoord5.xx), mix (tmpvar_4.xy, tmpvar_4.xz, vTexCoord5.xx), vTexCoord5.yy);
    highp vec3 tmpvar_31;
    tmpvar_31.z = 0.0;
    tmpvar_31.xy = vIntersection_17;
    highp vec4 tmpvar_32;
    tmpvar_32.zw = vec2(0.0, 0.0);
    tmpvar_32.x = fIntersectionAmount_16;
    tmpvar_32.y = mix (clamp (tmpvar_18, 0.0, 1.0), clamp (tmpvar_19, 0.0, 1.0), vTexCoord5.z);
    tmpvar_15.x = vTexCoord2.x;
    tmpvar_5 = vec3(0.0, 0.0, 0.0);
    tmpvar_6 = tmpvar_15;
    tmpvar_7 = tmpvar_30;
    tmpvar_8 = tmpvar_31;
    tmpvar_9 = tmpvar_32;
    tmpvar_10 = vec4(0.0, 0.0, 0.0, 0.0);
  } else {
    highp vec3 tmpvar_33;
    tmpvar_33.z = 0.0;
    tmpvar_33.xy = tmpvar_3.xy;
    highp vec3 tmpvar_34;
    tmpvar_34.z = 0.0;
    tmpvar_34.xy = tmpvar_3.xz;
    tmpvar_3 = mix (tmpvar_33, tmpvar_34, vTexCoord6.xxx);
    highp vec3 tmpvar_35;
    tmpvar_35.z = 0.0;
    tmpvar_35.xy = tmpvar_4.xy;
    highp vec3 tmpvar_36;
    tmpvar_36.z = 0.0;
    tmpvar_36.xy = tmpvar_4.xz;
    tmpvar_4 = mix (tmpvar_35, tmpvar_36, vTexCoord6.xxx);
    highp vec4 tmpvar_37;
    tmpvar_37 = mix (PenThicknessA, PenThicknessB, vTexCoord6.xxxx);
    highp vec3 tmpvar_38;
    highp vec3 tmpvar_39;
    tmpvar_38 = tmpvar_3;
    tmpvar_39 = tmpvar_4;
    highp vec3 tmpvar_40;
    highp vec2 tmpvar_41;
    highp vec4 tmpvar_42;
    highp float tmpvar_43;
    tmpvar_43 = vTexCoord5.x;
    highp float tmpvar_44;
    tmpvar_44 = abs(vTexCoord5.w);
    if ((tmpvar_44 == 0.0)) {
      tmpvar_39.y = tmpvar_3.y;
      tmpvar_39.x = (tmpvar_4.x - ((0.5 * tmpvar_37.x) * sign(
        (tmpvar_4.x - tmpvar_3.x)
      )));
      tmpvar_38.x = (tmpvar_3.x - ((0.5 * tmpvar_37.x) * sign(
        (tmpvar_39.x - tmpvar_3.x)
      )));
    } else {
      tmpvar_38.x = tmpvar_39.x;
      tmpvar_39.y = (tmpvar_39.y - ((0.5 * tmpvar_37.x) * sign(
        (tmpvar_39.y - tmpvar_3.y)
      )));
      tmpvar_38.y = (tmpvar_3.y - ((0.5 * tmpvar_37.x) * sign(
        (tmpvar_39.y - tmpvar_3.y)
      )));
    };
    highp vec3 x_45;
    x_45 = (tmpvar_38 - tmpvar_39);
    highp vec4 tmpvar_46;
    tmpvar_46.x = (tmpvar_37.x * vTexCoord5.z);
    tmpvar_46.y = tmpvar_43;
    tmpvar_46.z = tmpvar_37.x;
    tmpvar_46.w = ((sqrt(
      dot (x_45, x_45)
    ) / tmpvar_37.y) * abs(vTexCoord5.y));
    tmpvar_42.xzw = tmpvar_46.xzw;
    tmpvar_42.y = 0.5;
    tmpvar_41.x = vTexCoord2.y;
    tmpvar_5 = tmpvar_40;
    tmpvar_6 = tmpvar_41;
    tmpvar_7 = mix (tmpvar_38, tmpvar_39, vTexCoord5.yyy);
    tmpvar_8 = mix (tmpvar_39, tmpvar_38, vTexCoord5.yyy);
    tmpvar_9 = tmpvar_42;
    tmpvar_10 = vTexCoord6;
  };
  highp vec4 tmpvar_47;
  tmpvar_47.zw = vec2(0.0, 1.0);
  tmpvar_47.xy = tmpvar_5.xy;
  tmpvar_2 = (tmpvar_47 * vMatWorldViewProj_1);
  highp vec3 tmpvar_48;
  tmpvar_48 = vec3(0.0, 0.0, 0.0);
  highp vec4 tmpvar_49;
  highp float tmpvar_50;
  tmpvar_50 = abs(tmpvar_9.z);
  if ((tmpvar_50 > 0.0)) {
    highp vec4 outPosition_51;
    highp vec4 tmpvar_52;
    tmpvar_52.zw = vec2(0.0, 1.0);
    tmpvar_52.xy = tmpvar_7.xy;
    highp vec4 tmpvar_53;
    tmpvar_53 = (tmpvar_52 * matWorldViewProj);
    outPosition_51 = (tmpvar_53 / abs(tmpvar_53.w));
    highp vec4 tmpvar_54;
    tmpvar_54.zw = vec2(0.0, 1.0);
    tmpvar_54.xy = tmpvar_8.xy;
    highp vec4 tmpvar_55;
    tmpvar_55 = (tmpvar_54 * matWorldViewProj);
    highp vec2 tmpvar_56;
    tmpvar_56 = normalize(((tmpvar_55 / 
      abs(tmpvar_55.w)
    ).xy - outPosition_51.xy));
    outPosition_51.x = (outPosition_51.x - ((tmpvar_9.x * screenSize.z) * tmpvar_56.y));
    outPosition_51.y = (outPosition_51.y + ((tmpvar_9.x * screenSize.w) * tmpvar_56.x));
    tmpvar_49 = outPosition_51;
  } else {
    highp vec4 finalPosition_57;
    highp vec4 tmpvar_58;
    tmpvar_58.zw = vec2(0.0, 1.0);
    tmpvar_58.xy = tmpvar_7.xy;
    highp vec4 tmpvar_59;
    tmpvar_59 = (tmpvar_58 * matWorldViewProj);
    highp vec4 tmpvar_60;
    tmpvar_60.zw = vec2(0.0, 1.0);
    tmpvar_60.xy = tmpvar_8.xy;
    highp vec4 tmpvar_61;
    tmpvar_61 = (tmpvar_60 * matWorldViewProj);
    finalPosition_57.xyw = mix ((tmpvar_59 / abs(tmpvar_59.w)), (tmpvar_61 / abs(tmpvar_61.w)), tmpvar_9.xxxx).xyw;
    finalPosition_57.z = 0.25;
    tmpvar_49 = finalPosition_57;
  };
  if ((tmpvar_9.z > 0.0)) {
    tmpvar_48.x = (2.0 / tmpvar_9.z);
  } else {
    tmpvar_48.x = 0.0;
  };
  tmpvar_48.y = (1.0/(tmpvar_48.x));
  tmpvar_48.z = tmpvar_10.x;
  highp vec2 tmpvar_62;
  tmpvar_62.x = tmpvar_9.y;
  tmpvar_62.y = tmpvar_9.w;
  highp vec2 _position_63;
  _position_63 = (vec2(0.5, 0.5) * (tmpvar_49.xy + vec2(1.0, 1.0)));
  highp vec2 tmpvar_64;
  highp vec4 tmpvar_65;
  tmpvar_65.zw = vec2(0.0, 1.0);
  tmpvar_65.xy = _position_63;
  tmpvar_64 = (matTexCoordTransform * tmpvar_65).xy;
  _position_63 = tmpvar_64;
  highp vec2 tmpvar_66;
  tmpvar_66.y = 0.5;
  tmpvar_66.x = (PaletteSize.z * (tmpvar_6.x + 0.5));
  highp vec4 tmpvar_67;
  lowp vec4 tmpvar_68;
  tmpvar_68 = texture2DLod (PaletteTexture, tmpvar_66, 0.0);
  tmpvar_67 = tmpvar_68;
  highp float tmpvar_69;
  tmpvar_69 = sqrt(dot (tmpvar_67, tmpvar_67));
  highp float tmpvar_70;
  if ((tmpvar_69 > 0.0)) {
    tmpvar_70 = 1.0;
  } else {
    tmpvar_70 = 0.0;
  };
  tmpvar_48.y = tmpvar_70;
  tmpvar_2.xzw = tmpvar_49.xzw;
  tmpvar_2.y = -(tmpvar_49.y);
  gl_Position = tmpvar_2;
  xlv_COLOR0 = tmpvar_67;
  xlv_TEXCOORD0 = tmpvar_62;
  xlv_TEXCOORD1 = tmpvar_64;
  xlv_TEXCOORD2 = tmpvar_48;
}

