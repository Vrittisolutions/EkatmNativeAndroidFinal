struct sGridParams {
  highp vec4 ContourScale;
  highp vec4 ContourOffset;
  highp vec4 ContourThickness;
  highp vec4 ContourColorX;
  highp vec4 ContourColorY;
  highp vec4 ContourColorZ;
  highp vec4 Params;
  highp vec4 SurfaceNormal;
  highp vec4 Lighting;
};
uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 ClipPlanes[6];
uniform sGridParams GridParams;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 MeshCellOfssetScalePosition;
uniform highp vec4 MeshCellOfssetScaleTexCoord;
uniform highp vec4 MeshCellOfssetScaleHeights;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
varying highp vec4 xlv_COLOR4;
varying highp vec2 xlv_COLOR5;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_1.w = vPosition.w;
  tmpvar_2.zw = vTexCoord0.zw;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  highp vec4 tmpvar_6;
  highp vec2 tmpvar_7;
  highp mat4 tmpvar_8;
  tmpvar_8 = (matWorldView * matProj);
  tmpvar_1.xz = ((vPosition.xz * MeshCellOfssetScalePosition.xy) + MeshCellOfssetScalePosition.zw);
  highp vec2 _texCoord_9;
  _texCoord_9 = ((vTexCoord0.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_9 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_9);
  lowp vec4 tmpvar_10;
  tmpvar_10 = texture2DLod (HeightMapTexture, _texCoord_9, 0.0);
  tmpvar_1.y = (tmpvar_10.x * vPosition.w);
  highp vec3 tmpvar_11;
  highp mat3 tmpvar_12;
  tmpvar_12[0] = matWorld[0].xyz;
  tmpvar_12[1] = matWorld[1].xyz;
  tmpvar_12[2] = matWorld[2].xyz;
  tmpvar_11 = (GridParams.SurfaceNormal.xyz * tmpvar_12);
  highp vec2 _texCoord_13;
  _texCoord_13 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_13 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_13);
  lowp vec4 tmpvar_14;
  tmpvar_14 = texture2DLod (HeightMapTexture, _texCoord_13, 0.0);
  highp float tmpvar_15;
  tmpvar_15 = GridParams.Params.w;
  highp float tmpvar_16;
  tmpvar_16 = mix (tmpvar_1.y, (tmpvar_14.x * vPosition.w), tmpvar_15);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_15));
  tmpvar_5 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_4 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  highp vec4 tmpvar_17;
  tmpvar_17 = (tmpvar_1 * matWorld);
  bvec4 tmpvar_18;
  tmpvar_18 = bvec4(ClipPlanes[0]);
  bool tmpvar_19;
  tmpvar_19 = any(tmpvar_18);
  highp float tmpvar_20;
  if (tmpvar_19) {
    highp vec4 tmpvar_21;
    tmpvar_21.w = 1.0;
    tmpvar_21.xyz = tmpvar_17.xyz;
    tmpvar_20 = dot (tmpvar_21, ClipPlanes[0]);
  } else {
    tmpvar_20 = 1.0;
  };
  tmpvar_6.x = tmpvar_20;
  bvec4 tmpvar_22;
  tmpvar_22 = bvec4(ClipPlanes[1]);
  bool tmpvar_23;
  tmpvar_23 = any(tmpvar_22);
  highp float tmpvar_24;
  if (tmpvar_23) {
    highp vec4 tmpvar_25;
    tmpvar_25.w = 1.0;
    tmpvar_25.xyz = tmpvar_17.xyz;
    tmpvar_24 = dot (tmpvar_25, ClipPlanes[1]);
  } else {
    tmpvar_24 = 1.0;
  };
  tmpvar_6.y = tmpvar_24;
  bvec4 tmpvar_26;
  tmpvar_26 = bvec4(ClipPlanes[2]);
  bool tmpvar_27;
  tmpvar_27 = any(tmpvar_26);
  highp float tmpvar_28;
  if (tmpvar_27) {
    highp vec4 tmpvar_29;
    tmpvar_29.w = 1.0;
    tmpvar_29.xyz = tmpvar_17.xyz;
    tmpvar_28 = dot (tmpvar_29, ClipPlanes[2]);
  } else {
    tmpvar_28 = 1.0;
  };
  tmpvar_6.z = tmpvar_28;
  bvec4 tmpvar_30;
  tmpvar_30 = bvec4(ClipPlanes[3]);
  bool tmpvar_31;
  tmpvar_31 = any(tmpvar_30);
  highp float tmpvar_32;
  if (tmpvar_31) {
    highp vec4 tmpvar_33;
    tmpvar_33.w = 1.0;
    tmpvar_33.xyz = tmpvar_17.xyz;
    tmpvar_32 = dot (tmpvar_33, ClipPlanes[3]);
  } else {
    tmpvar_32 = 1.0;
  };
  tmpvar_6.w = tmpvar_32;
  bvec4 tmpvar_34;
  tmpvar_34 = bvec4(ClipPlanes[4]);
  bool tmpvar_35;
  tmpvar_35 = any(tmpvar_34);
  highp float tmpvar_36;
  if (tmpvar_35) {
    highp vec4 tmpvar_37;
    tmpvar_37.w = 1.0;
    tmpvar_37.xyz = tmpvar_17.xyz;
    tmpvar_36 = dot (tmpvar_37, ClipPlanes[4]);
  } else {
    tmpvar_36 = 1.0;
  };
  tmpvar_7.x = tmpvar_36;
  bvec4 tmpvar_38;
  tmpvar_38 = bvec4(ClipPlanes[5]);
  bool tmpvar_39;
  tmpvar_39 = any(tmpvar_38);
  highp float tmpvar_40;
  if (tmpvar_39) {
    highp vec4 tmpvar_41;
    tmpvar_41.w = 1.0;
    tmpvar_41.xyz = tmpvar_17.xyz;
    tmpvar_40 = dot (tmpvar_41, ClipPlanes[5]);
  } else {
    tmpvar_40 = 1.0;
  };
  tmpvar_7.y = tmpvar_40;
  tmpvar_1.w = 1.0;
  tmpvar_3.xy = tmpvar_2.xy;
  tmpvar_3.zw = tmpvar_2.zw;
  gl_Position = (tmpvar_1 * tmpvar_8);
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
  xlv_TEXCOORD2 = tmpvar_11;
  xlv_TEXCOORD6 = tmpvar_16;
  xlv_TEXCOORD7 = tmpvar_5;
  xlv_COLOR4 = tmpvar_6;
  xlv_COLOR5 = tmpvar_7;
}

